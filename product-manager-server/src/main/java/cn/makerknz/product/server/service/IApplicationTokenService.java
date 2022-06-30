package cn.makerknz.product.server.service;

import cn.makerknz.product.server.domain.form.EmqxWebhookForm;
import cn.makerknz.product.server.entity.ApplicationToken;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;

import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
public interface IApplicationTokenService extends IService<ApplicationToken> {

    void sendDeviceEventsToApplication(Integer productId, EmqxWebhookForm emqxWebhookForm);

    ApplicationToken createPulsarToken(ApplicationToken applicationToken) throws IOException, PulsarAdminException;
}
