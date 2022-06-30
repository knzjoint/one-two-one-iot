package cn.makerknz.product.server.service;

import cn.makerknz.product.server.entity.ApplicationToken;
import org.apache.pulsar.client.admin.PulsarAdminException;

/**
 * @Author: maker_knz
 * @Date: 2022/2/25/025 10:14
 * @Version 1.0
 */

public interface IPulsarService {

    void pulsarAuthorization(String tenantName, String namespace, String role) throws PulsarAdminException;

}
