package cn.makerknz.product.server.service;

import cn.makerknz.product.server.domain.param.DeviceParam;
import cn.makerknz.product.server.domain.vo.DeviceVO;
import cn.makerknz.product.server.entity.Device;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
public interface IDeviceService extends IService<Device> {

    Mono<Device> findByClientId(String clientId);

    Mono<Device> findByUsername(String username);

    Flux<Device> findByProductId(Integer productId);

    Device createDevice(Device device);

    boolean deleteDeviceById(Integer deviceId, Integer userId);

    Page<DeviceVO> selectPageByWrapper(Page<Device> page, DeviceParam deviceParam);

}
