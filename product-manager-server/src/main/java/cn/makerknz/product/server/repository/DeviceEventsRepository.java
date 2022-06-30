package cn.makerknz.product.server.repository;

import cn.makerknz.product.server.entity.DeviceEvents;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface DeviceEventsRepository extends ReactiveCrudRepository<DeviceEvents, Integer> {
}
