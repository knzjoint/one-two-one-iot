package cn.makerknz.product.server.controller;


import cn.makerknz.product.server.annotation.CheckLogin;
import cn.makerknz.product.server.entity.Device;
import cn.makerknz.product.server.entity.MqttAcl;
import cn.makerknz.product.server.entity.Product;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.exception.ResultVO;
import cn.makerknz.product.server.service.IDeviceService;
import cn.makerknz.product.server.service.IMqttAclService;
import cn.makerknz.product.server.service.IProductService;
import cn.makerknz.product.server.utils.TopicUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author maker knz
 * @since 2022-03-23
 */
@RestController
@Slf4j
@RequestMapping("/topic")
public class MqttAclController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IMqttAclService mqttAclService;

    @Autowired
    private IDeviceService deviceService;

    @GetMapping("/list/{id}")
    @CheckLogin
    public ResultVO topicList(@PathVariable("id") Integer productId, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer)httpServletRequest.getAttribute("id");

        Product product = productService.getById(productId);
        if(!product.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionEnum.USER_PERMISSION_ERROR);
        }

        QueryWrapper<MqttAcl> queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", productId);
        List<MqttAcl> mqttAcls = mqttAclService.list(queryWrapper);

        // 修改主题内容
        List<MqttAcl> mqttAclList = mqttAcls.stream().map(e -> {
            e.setTopic(TopicUtils.replaceToDevice(e.getTopic()));
            return e;
        }).collect(Collectors.toList());

        return ResultVO.success(mqttAclList);
    }

    /**
     * 设备的主题列表
     * @param deviceId
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/list/device/{id}")
    @CheckLogin
    public ResultVO deviceTopicList(@PathVariable("id") Integer deviceId, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer)httpServletRequest.getAttribute("id");

        Device device = deviceService.getById(deviceId);
        if(!device.getUserId().equals(userId)) {
            log.error("权限错误");
            throw new BusinessException(ExceptionEnum.USER_PERMISSION_ERROR);
        }

        QueryWrapper<MqttAcl> queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", device.getProductId());
        queryWrapper.eq("clientid", device.getClientId());
        List<MqttAcl> mqttAcls = mqttAclService.list(queryWrapper);

        // 修改主题内容
        List<MqttAcl> mqttAclList = mqttAcls.stream().map(e -> {
//            e.setTopic(e.getTopic().replaceAll("%u", device.getUsername()));
            e.setTopic(TopicUtils.replaceToDevice(e.getTopic(), device.getClientId()));
            return e;
        }).collect(Collectors.toList());

        return ResultVO.success(mqttAclList);
    }


}
