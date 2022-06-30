package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.service.IPulsarService;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.common.policies.data.AuthAction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PulsarServiceImplTest {

    @Autowired
    private PulsarServiceImpl pulsarService;

    @Test
    void createApp() {
    }

    @Test
    void authorization() throws PulsarAdminException {
        Set<AuthAction> actions = new HashSet<>();
        actions.add(AuthAction.consume);
        actions.add(AuthAction.produce);
        pulsarService.authorization("my-tenant", "my-namespace", "test", actions);
    }

    @Test
    void add() throws PulsarAdminException {
        String tenantName = "test-a";
        String namespace = "namespace";
        pulsarService.addTenant(tenantName);
        pulsarService.addNamespace(tenantName, namespace);
        Set<AuthAction> actions = new HashSet<>();
        actions.add(AuthAction.consume);
        actions.add(AuthAction.produce);
    }

    @Test
    void unauthorization() {
    }

    @Test
    void addTenant() {
        try {
            pulsarService.addTenant("test");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void addNamespace() {
        try {
            pulsarService.addNamespace("my-tenant", "test");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}