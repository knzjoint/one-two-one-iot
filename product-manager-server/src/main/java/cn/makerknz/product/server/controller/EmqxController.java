package cn.makerknz.product.server.controller;

import cn.makerknz.product.server.annotation.CheckEmqxWebhookIdentity;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.domain.form.EmqxWebhookForm;
import cn.makerknz.product.server.exception.ResultVO;
import cn.makerknz.product.server.entity.Device;
import cn.makerknz.product.server.entity.DeviceEvents;
import cn.makerknz.product.server.service.IApplicationTokenService;
import cn.makerknz.product.server.service.IDeviceEventsService;
import cn.makerknz.product.server.service.IDeviceService;
import org.apache.pulsar.shade.com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/emqx")
public class EmqxController {

    @Autowired
    private IDeviceEventsService deviceEventsService;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IApplicationTokenService applicationTokenService;

    private Gson gson = new Gson();

    @CheckEmqxWebhookIdentity
    @PostMapping("/webhook")
    public Mono<ResultVO<Object>> webhook(@RequestBody EmqxWebhookForm emqxWebhookForm) {

        // 1.查看设备并查找到产品

        return deviceService.findByClientId(emqxWebhookForm.getClientid()).flatMap(e -> {
            // 消息转发到指定的应用
             applicationTokenService.sendDeviceEventsToApplication(e.getProductId(), emqxWebhookForm);

            // 存储消息
            DeviceEvents deviceEvents = DeviceEvents.builder()
                    .productId(e.getProductId())
                    .data(gson.toJson(emqxWebhookForm))
                    .deviceId(e.getId())
                    .eventAction(emqxWebhookForm.getAction())
                    .streamId(UUID.randomUUID().toString())
                    .topic(emqxWebhookForm.getTopic())
                    .dataType(1)
//                    .msgTime(emqxWebhookForm.getConnected_at())
                    .build();
            // 更新
            if (emqxWebhookForm.getAction().equals("client_connected")) {
                Device device = new Device();
                BeanUtils.copyProperties(e, device);
                device.setConnectStatus(true);
                deviceService.updateById(device);
            } else if (emqxWebhookForm.getAction().equals("client_disconnected")) {
                Device device = new Device();
                BeanUtils.copyProperties(e, device);
                device.setConnectStatus(false);
                deviceService.updateById(device);
            }

            return deviceEventsService.add(deviceEvents).then(Mono.just(ResultVO.success()));
        }).defaultIfEmpty(ResultVO.error(ExceptionEnum.INTERNAL_SERVER_ERROR));
    }

}
