package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.domain.param.DeviceParam;
import cn.makerknz.product.server.domain.vo.DeviceVO;
import cn.makerknz.product.server.entity.Device;
import cn.makerknz.product.server.entity.MqttAcl;
import cn.makerknz.product.server.entity.MqttUser;
import cn.makerknz.product.server.mapper.DeviceMapper;
import cn.makerknz.product.server.repository.DeviceRepository;
import cn.makerknz.product.server.service.IDeviceService;
import cn.makerknz.product.server.service.IMqttAclService;
import cn.makerknz.product.server.service.IMqttUserService;
import cn.makerknz.product.server.utils.Md5SaltUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private IMqttAclService mqttAclService;

    @Autowired
    private IMqttUserService mqttUserService;

    @Override
    public Mono<Device> findByClientId(String clientId) {
        return deviceRepository.findByClientId(clientId);
    }

    @Override
    public Mono<Device> findByUsername(String username) {
        return deviceRepository.findByUsername(username);
    }

    @Override
    public Flux<Device> findByProductId(Integer productId) {
        return deviceRepository.findByProductId(productId);
    }

    @Override
    public Device createDevice(Device device) {
        // 创建Mqtt的密钥
        // 生成随机盐
        String salt = Md5SaltUtils.getSalt();
        String password = Md5SaltUtils.getSalt();
        String md5Encryption = Md5SaltUtils.getMD5Encryption(password, salt);
        // 连接多个数据库
        device.setPassword(password);
        boolean save = this.save(device);
        if (!save) {
            throw new RuntimeException("设备创建错误");
        }

        return device;
    }

    @Override
    public boolean deleteDeviceById(Integer deviceId, Integer userId) {
        Device device = this.getById(deviceId);
        if (device == null) {
            throw new RuntimeException("获取设备信息失败!");
        }
        if (!device.getUserId().equals(userId)) {
            throw new RuntimeException("没有删除的权限!");
        }

        // 删除设备所有的主题
        QueryWrapper<MqttAcl> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", device.getUsername());
        mqttAclService.remove(queryWrapper);

        // 删除设备
        boolean isDeleted = false;
        isDeleted = this.removeById(deviceId);
        if (!isDeleted) {
            throw new RuntimeException("删除设备失败");
        }
        // 删除设备账号
        QueryWrapper<MqttUser> mqttUserQueryWrapper = new QueryWrapper<>();
        mqttUserQueryWrapper.eq("username", device.getUsername());
        isDeleted = this.mqttUserService.remove(mqttUserQueryWrapper);
        return isDeleted;
    }

    @Override
    public Page<DeviceVO> selectPageByWrapper(Page<Device> page, DeviceParam deviceParam) {

        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("d.user_id", deviceParam.getUserId())
                .eq("d.deleted", false)
                .in(deviceParam.getConnectStatus() != null, "connect_status", deviceParam.getConnectStatus())
                .in(deviceParam.getProductName() != null, "product_id", deviceParam.getProductName());

        return baseMapper.selectPageByWrapper(page, queryWrapper);
    }
}
