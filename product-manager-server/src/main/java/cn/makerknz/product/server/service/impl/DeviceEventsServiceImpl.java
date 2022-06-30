package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.entity.DeviceEvents;
import cn.makerknz.product.server.mapper.DeviceEventsMapper;
import cn.makerknz.product.server.repository.DeviceEventsRepository;
import cn.makerknz.product.server.service.IDeviceEventsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
public class DeviceEventsServiceImpl extends ServiceImpl<DeviceEventsMapper, DeviceEvents> implements IDeviceEventsService {

    @Autowired
    private DeviceEventsRepository deviceEventsRepository;

    @Override
    public Mono<DeviceEvents> add(DeviceEvents deviceEvents) {
        return deviceEventsRepository.save(deviceEvents);
    }

}
