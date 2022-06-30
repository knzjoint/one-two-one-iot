package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.entity.Product;
import cn.makerknz.product.server.service.IMqttAclService;
import cn.makerknz.product.server.service.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private IProductService productService;

    @Autowired
    private IMqttAclService mqttAclService;

    @Test
    public void save() {
    }

}