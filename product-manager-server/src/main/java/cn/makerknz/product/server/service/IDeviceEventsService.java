package cn.makerknz.product.server.service;

import cn.makerknz.product.server.entity.DeviceEvents;
import com.baomidou.mybatisplus.extension.service.IService;
import reactor.core.publisher.Mono;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
public interface IDeviceEventsService extends IService<DeviceEvents> {

    Mono<DeviceEvents> add(DeviceEvents deviceEvents);

}
