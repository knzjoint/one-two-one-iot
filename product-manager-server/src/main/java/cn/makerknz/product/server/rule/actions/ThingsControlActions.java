package cn.makerknz.product.server.rule.actions;

import cn.makerknz.product.server.domain.format.DataTypeEnum;
import cn.makerknz.product.server.domain.format.ThingsValue;
import cn.makerknz.product.server.emqx.EmqxAdminService;
import cn.makerknz.product.server.emqx.Message;
import cn.makerknz.product.server.emqx.ThingsPayloadVO;
import cn.makerknz.product.server.entity.*;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.rule.EasyRuleUtils;
import cn.makerknz.product.server.rule.core.domain.LinkTypeEnum;
import cn.makerknz.product.server.service.*;
import cn.makerknz.product.server.utils.SpringBeanUtil;
import cn.makerknz.product.server.websocket.MessageCommandEnum;
import cn.makerknz.product.server.websocket.WebSocketMessage;
import cn.makerknz.product.server.websocket.WebSocketServer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/6 15:59
 */
public class ThingsControlActions {

    public static void controlThings(Integer thingsId, ThingsValue thingsValue) {

        IThingsService thingsService = SpringBeanUtil.getBean(IThingsService.class);
        IThingsLinkageService thingsLinkageService = SpringBeanUtil.getBean(IThingsLinkageService.class);
        IThingsLinkageLinkService thingsLinkageLinkService = SpringBeanUtil.getBean(IThingsLinkageLinkService.class);
        RulesEngine rulesEngine = SpringBeanUtil.getBean(RulesEngine.class);

        Things things = thingsService.getById(thingsId);

        ThingsControlActions.sendAndUpdateMessage(things, thingsValue);

        /******            控制物           ****/
        // 1、获取联动的列表
        ThingsLinkageLink thingsLinkageLink = ThingsLinkageLink.builder()
                .linkId(things.getId())
                .linkType(LinkTypeEnum.THINGS_TYPE)
                .build();
        QueryWrapper<ThingsLinkageLink> queryWrapper = new QueryWrapper<>(thingsLinkageLink);
        List<ThingsLinkageLink> thingsLinkageLinks = thingsLinkageLinkService.list(queryWrapper);
        List<Integer> thingsLinkageLinkIds = thingsLinkageLinks.stream().map(e -> e.getThingsLinkageId()).distinct().collect(Collectors.toList());

        thingsLinkageLinkIds.forEach(e -> {
            ThingsLinkage linkage = thingsLinkageService.getById(e);

            String rulesString = linkage.getRule();
            Rules rules = EasyRuleUtils.createRules(rulesString);
            Facts facts = new Facts();

            QueryWrapper<ThingsLinkageLink> queryThingsLinkageLinkWrapper = new QueryWrapper<>();
            List<ThingsLinkageLink> list = thingsLinkageLinkService.list(queryThingsLinkageLinkWrapper);
            List<Integer> collect = list.stream().map(link -> link.getLinkId()).collect(Collectors.toList());
            List<Things> _things = thingsService.listByIds(collect);
            _things.stream().forEach(thing -> {
                String name = "THINGS_" + thing.getId() + "_" + thing.getSequence();
                if (DataTypeEnum.STRING.equals(thing.getThingsType()) || DataTypeEnum.JSON.equals(thing.getThingsType())) {
                    facts.put(name, "'" + thing.getThingsValue() + "'");
                } else if (DataTypeEnum.INT.equals(thing.getThingsType())) {
                    facts.put(name, thing.getThingsValue().equals("true") ? true : false);
                } else {
                    facts.put(name, thing.getThingsValue());
                }

            });

            rulesEngine.fire(rules, facts);
        });
    }

    public static void ruleEngineControl(Integer thingsId, ThingsValue thingsValue) {
        IThingsService thingsService = SpringBeanUtil.getBean(IThingsService.class);
        Things things = thingsService.getById(thingsId);
        ThingsControlActions.sendAndUpdateMessage(things, thingsValue);
    }

    static void sendAndUpdateMessage(Things things, ThingsValue thingsValue) {

        IDeviceService deviceService = SpringBeanUtil.getBean(IDeviceService.class);
        IProductService productService = SpringBeanUtil.getBean(IProductService.class);
        EmqxAdminService emqxAdminService = SpringBeanUtil.getBean(EmqxAdminService.class);
        IThingsService thingsService = SpringBeanUtil.getBean(IThingsService.class);
        Gson gson = SpringBeanUtil.getBean(Gson.class);

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

        // 更新模型默认数据
        thingsService.updateById(things);
    }

}
