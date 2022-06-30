package cn.makerknz.product.server.repository;

import cn.makerknz.product.server.entity.Device;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeviceRepository extends ReactiveCrudRepository<Device, Integer> {

    Mono<Device> findByClientId(String clientId);

    Mono<Device> findByUsername(String username);

    Flux<Device> findByProductId(Integer productId);

}
