package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.config.PulsarConfig;
import cn.makerknz.product.server.domain.form.EmqxWebhookForm;
import cn.makerknz.product.server.entity.ApplicationToken;
import cn.makerknz.product.server.entity.User;
import cn.makerknz.product.server.mapper.ApplicationTokenMapper;
import cn.makerknz.product.server.service.IApplicationTokenService;
import cn.makerknz.product.server.service.IPulsarService;
import cn.makerknz.product.server.service.IUserService;
import cn.makerknz.product.server.utils.ApplicationTokenUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.shade.com.google.gson.Gson;
import org.apache.pulsar.shade.org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */

@Service
@Slf4j
public class ApplicationTokenServiceImpl extends ServiceImpl<ApplicationTokenMapper, ApplicationToken> implements IApplicationTokenService {

    @Autowired
    private PulsarConfig pulsarConfig;

    @Autowired
    private IUserService userService;

    @Autowired
    private ApplicationTokenUtils applicationTokenUtils;

    @Autowired
    private IPulsarService pulsarService;

    private Gson gson = new Gson();

    @Override
    public void sendDeviceEventsToApplication(Integer productId, EmqxWebhookForm emqxWebhookForm) {
        QueryWrapper<ApplicationToken> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);

        List<ApplicationToken> applicationTokens = this.baseMapper.selectList(queryWrapper);

        log.info(gson.toJson(emqxWebhookForm));

        applicationTokens.stream().forEach(e -> {
            try {
                // TODO 之后是否要转换成
                // 1. 创建Pulsar客户端
                PulsarClient client = PulsarClient.builder()
                        .serviceUrl(pulsarConfig.getServer())
                        .authentication(AuthenticationFactory.token(e.getToken()))
                        .build();

                String topic = "persistent://" + e.getTenantName() + "/" + e.getNamespace() + "/" + "emqx";

//                // 2. 选择发布的主题
//                Producer<EmqxWebhookForm> emqxWebhookFormProducer = client.newProducer(Schema.JSON(EmqxWebhookForm.class))
//                        .topic(topic)
//                        .create();
//                // 3. 发布消息
//                emqxWebhookFormProducer.send(emqxWebhookForm);

                // 2. 选择发布的主题
                Producer<byte[]> emqxWebhookFormProducer = client.newProducer()
                        .topic(topic)
                        .create();
                // 3. 发布消息
//                emqxWebhookFormProducer.send(gson.toJson(emqxWebhookForm));
                emqxWebhookFormProducer.send(gson.toJson(emqxWebhookForm).getBytes());

                // 4. 关闭客户端
                client.close();
            } catch (PulsarClientException ex) {
                log.error("应用" + e.getApplicationName() + "发送错误！");
                ex.printStackTrace();
            }
        });
    }

    @Override
    public ApplicationToken createPulsarToken(ApplicationToken applicationToken) throws IOException, PulsarAdminException {
        // 创建租户
        User user = userService.getById(applicationToken.getUserId());

        // 生成命名空间
        String namespace = RandomStringUtils.randomAlphanumeric(6);

        // 创建token
        String token = applicationTokenUtils.generateToken(new HashMap<>(), namespace);
        applicationToken.setToken(token);

        applicationToken.setRole(namespace);
        applicationToken.setNamespace(namespace);
        applicationToken.setTenantName(user.getUsername());

        boolean save = this.save(applicationToken);
        if (!save) {
            throw new RuntimeException("错误");
        }

        // 在Pulsar创建租户/命名空间，并进行授权
        pulsarService.pulsarAuthorization(user.getUsername(), namespace, namespace);

        return applicationToken;
    }
}
