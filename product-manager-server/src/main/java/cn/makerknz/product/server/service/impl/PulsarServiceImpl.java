package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.service.IPulsarService;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.common.policies.data.AuthAction;
import org.apache.pulsar.common.policies.data.TenantInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @Author: maker_knz
 * @Date: 2022/2/25/025 10:18
 * @Version 1.0
 */

@Service
public class PulsarServiceImpl implements IPulsarService {

    @Autowired
    private PulsarAdmin pulsarAdmin;

    /**
     * 授权
     * @param tenantName
     * @param namespace
     * @param role
     * @throws PulsarAdminException
     */
    @Override
    public void pulsarAuthorization(String tenantName, String namespace, String role) throws PulsarAdminException {
        this.addTenant(tenantName);
        this.addNamespace(tenantName, namespace);
        Set<AuthAction> actions = new HashSet<>();
        actions.add(AuthAction.consume);
        actions.add(AuthAction.produce);

        // 1、给应用授权
        this.authorization(tenantName, namespace, role, actions);

        // 2.给admin授权生产权限
        // 授权给admin 角色一个生产的权限
        Set<AuthAction> adminActions = new HashSet<>();
        adminActions.add(AuthAction.produce);
        this.authorization(tenantName, namespace, "admin", adminActions);
    }

    /**
     * admin 可以进行租户/命名空间/主题 对角色进行授权
     */
    public boolean authorization(String tenantName, String namespace, String role, Set<AuthAction> actions) throws PulsarAdminException {
        pulsarAdmin.namespaces().grantPermissionOnNamespace(tenantName + "/" + namespace, role, actions);

        return true;
    }

    /**
     * 取消授权
     * @param namespace
     * @param role
     * @return
     * @throws PulsarAdminException
     */
    public boolean unauthorization(String namespace, String role) throws PulsarAdminException {
        pulsarAdmin.namespaces().revokePermissionsOnNamespace(namespace, role);
        return true;
    }

    /**
     * 添加租户
     * @param tenantName
     * @throws PulsarAdminException
     */
    public void addTenant(String tenantName) throws PulsarAdminException {
        if (isTenantsExisted(tenantName)) {
            return;
        }

        Set<String> adminRoles = new HashSet<>();
        adminRoles.add("admin");

        // 单节点时使用standalone
        Set<String> clusters = new HashSet<>();
        clusters.add("standalone");

        TenantInfo tenantInfo = TenantInfo.builder()
                .allowedClusters(clusters)
                .adminRoles(adminRoles)
                .build();
        pulsarAdmin.tenants().createTenant(tenantName, tenantInfo);
    }

    /**
     * 添加命名空间
     * @param namespace
     * @throws PulsarAdminException
     */
    public void addNamespace(String tenantName, String namespace) throws PulsarAdminException {
        pulsarAdmin.namespaces().createNamespace(tenantName + "/" + namespace);
    }

    private boolean isTenantsExisted(String tenantsName) throws PulsarAdminException {
        List<String> tenants = pulsarAdmin.tenants().getTenants();
        return tenants.contains(tenantsName);
    }
}
