package cn.makerknz.product.server.controller;


import cn.makerknz.product.server.annotation.CheckLogin;
import cn.makerknz.product.server.domain.form.DeviceForm;
import cn.makerknz.product.server.domain.param.DeviceParam;
import cn.makerknz.product.server.entity.Device;
import cn.makerknz.product.server.entity.MqttAcl;
import cn.makerknz.product.server.entity.MqttUser;
import cn.makerknz.product.server.entity.ProductTopic;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.exception.ResultVO;
import cn.makerknz.product.server.service.IDeviceService;
import cn.makerknz.product.server.service.IMqttAclService;
import cn.makerknz.product.server.service.IMqttUserService;
import cn.makerknz.product.server.service.IProductTopicService;
import cn.makerknz.product.server.utils.Md5SaltUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.shade.org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
@RestController
@RequestMapping("/device")
@Slf4j
public class DeviceController {

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IMqttUserService mqttUserService;

    @Autowired
    private IProductTopicService productTopicService;

    @Autowired
    private IMqttAclService mqttAclService;

    /**
     * 分页显示设备,可以根据设备进行查找
     *
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/list")
    @CheckLogin
    ResultVO getDeviceList(HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        Device device = Device.builder()
                .userId(userId)
                .build();
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>(device);
        List<Device> devices = deviceService.list(deviceQueryWrapper);
        return ResultVO.success(devices);
    }

    /**
     * 分页显示设备,可以根据设备进行查找
     *
     * @param page
     * @param deviceParam
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/page")
    @CheckLogin
    ResultVO getPage(Page page, DeviceParam deviceParam, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");
        deviceParam.setUserId(userId);
        Page applicationTokenPage = deviceService.selectPageByWrapper(page, deviceParam);
        return ResultVO.success(applicationTokenPage);
    }

    /**
     * 一个产品可以设置多个应用
     *
     * @param deviceForm
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/add-device")
    @CheckLogin
    ResultVO addDevice(@RequestBody DeviceForm deviceForm, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        // 同一产品查找设备名是否重复,且是没有删除的设备
        QueryWrapper<Device> queryWrapperDevice = new QueryWrapper<>();
        queryWrapperDevice.eq("username", deviceForm.getUsername());
        queryWrapperDevice.eq("product_id", deviceForm.getProductId());
        queryWrapperDevice.eq("deleted", false);
        List<Device> devices = this.deviceService.list(queryWrapperDevice);
        if (devices.size() >= 1) {
            log.error("添加Mqtt用户失败");
            throw new BusinessException(ExceptionEnum.DEVICE_EXISTED_ERROR);
        }

        Device device = new Device();
        BeanUtils.copyProperties(deviceForm, device);
        device.setUserId(userId);

        // 生成设备密码
        String password = deviceForm.getPassword() == null ?
                RandomStringUtils.randomAlphanumeric(8) : deviceForm.getPassword();
        String clientId = UUID.randomUUID().toString().replaceAll("-", ""); // clientID通过UUID生成
        String salt = Md5SaltUtils.getSalt();
        String md5Encryption = Md5SaltUtils.getMD5Encryption(password, salt);

        MqttUser mqttUser = MqttUser.builder()
                .password(md5Encryption)
                .username(deviceForm.getUsername())
                .clientId(clientId)
                .isSuperuser(false)
                .salt(salt)
                .build();
        boolean isMqttUser = this.mqttUserService.save(mqttUser);
        if (!isMqttUser) {
            log.error("添加Mqtt用户失败");
            throw new BusinessException(ExceptionEnum.MQTT_ADD_USER_ERROR);
        }

        // 存储设备
        device.setMqttUserId(mqttUser.getId());
        device.setPassword(password);
        device.setClientId(clientId);

        boolean isDeviceSaved = this.deviceService.save(device);
        if (!isDeviceSaved) {
            log.error("添加设备错误");
            throw new BusinessException(ExceptionEnum.MQTT_ADD_USER_ERROR);        }

        // 添加topic
        QueryWrapper<ProductTopic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", device.getProductId());
        List<ProductTopic> productTopics = productTopicService.list(queryWrapper);
        productTopics.stream().forEach(e -> {
            MqttAcl mqttAcl = new MqttAcl();
            BeanUtils.copyProperties(e, mqttAcl);
            mqttAcl.setUsername(device.getUsername());
            mqttAcl.setClientid(device.getClientId());
            mqttAcl.setId(null);
            mqttAcl.setProductTopicId(e.getId());

            mqttAclService.save(mqttAcl);
        });

        return ResultVO.success();
    }

    /**
     * 删除设备
     *
     * @param id                 设备ID
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping("/{id}")
    @CheckLogin
    ResultVO deleteDevice(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        boolean isDeleted = this.deviceService.deleteDeviceById(id, userId);
        if (!isDeleted) {
            throw new BusinessException(ExceptionEnum.MQTT_DELETED_USER_ERROR);
        }

        return ResultVO.success();
    }
}