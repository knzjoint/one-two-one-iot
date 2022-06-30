package cn.makerknz.product.server.utils;

import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationTokenUtilsTest {

    @Autowired
    private ApplicationTokenUtils applicationTokenUtils;

    @Test
    void generateToken() throws IOException {
        String token = applicationTokenUtils.generateToken(new HashMap<>(), "test");
        System.out.println(token);
        Assert.isTrue(token != null);
    }
}