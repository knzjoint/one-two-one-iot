package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.domain.format.ThingsValue;
import cn.makerknz.product.server.emqx.EmqxAdminService;
import cn.makerknz.product.server.emqx.Message;
import cn.makerknz.product.server.emqx.ThingsPayloadVO;
import cn.makerknz.product.server.entity.Device;
import cn.makerknz.product.server.entity.Product;
import cn.makerknz.product.server.entity.Things;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.mapper.ThingsMapper;
import cn.makerknz.product.server.service.IDeviceService;
import cn.makerknz.product.server.service.IProductService;
import cn.makerknz.product.server.service.IThingsService;
import cn.makerknz.product.server.utils.SpringBeanUtil;
import cn.makerknz.product.server.websocket.MessageCommandEnum;
import cn.makerknz.product.server.websocket.WebSocketMessage;
import cn.makerknz.product.server.websocket.WebSocketServer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author maker knz
 * @since 2022-04-20
 */
@Service
public class ThingsServiceImpl extends ServiceImpl<ThingsMapper, Things> implements IThingsService {

    @Autowired
    static IDeviceService deviceService;

    @Autowired
    static IProductService productService;

    @Autowired
    static EmqxAdminService emqxAdminService;

    @Autowired
    static IThingsService thingsService;

    @Autowired
    static Gson gson;

    public static void controlThings(Integer thingsId, ThingsValue thingsValue) {
        EmqxAdminService bean = SpringBeanUtil.getBean(EmqxAdminService.class);

        Things things = thingsService.getById(thingsId);


        // topic 构造
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
        deviceQueryWrapper.eq("client_id", things.getClientId());
        Device device = deviceService.getOne(deviceQueryWrapper);
        if (device == null) {
            throw new BusinessException(ExceptionEnum.THINGS_USER_NO_PERMISSION);
        }
        Product product = productService.getById(device.getProductId());
        String topic = "/" + product.getProductEnName() + "/" + device.getClientId() + "/things/" + things.getThingsName() + "/" + things.getSequence();

        // 构造payload
        ThingsPayloadVO thingsPayloadVO = new ThingsPayloadVO();
        BeanUtils.copyProperties(things, thingsPayloadVO);
        Message message = Message.builder()
                .type(thingsValue.getType())
                .value(thingsValue.getValue())
                .build();
        thingsPayloadVO.setMessage(message);
        String payload = gson.toJson(thingsPayloadVO);

        emqxAdminService.sendMsgToDevice(things.getClientId(), topic, payload);

        // 更改设备的当前值
        things.setThingsValue(thingsValue.getValue());
        // 通过websokcet将
        WebSocketMessage<Object> webSocketMessage = WebSocketMessage.builder()
                .command(MessageCommandEnum.THINGS_CHANGED)
                .message(things)
                .build();
        WebSocketServer.sendInfo(gson.toJson(webSocketMessage), things.getUserId());
    }

}
