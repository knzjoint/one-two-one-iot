package cn.makerknz.product.server.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Md5SaltUtilsTest {

    @Test
    public void md5(){
        String password="123456";
        //生成随机盐
        String salt = Md5SaltUtils.getSalt();

        String md5Encryption = Md5SaltUtils.getMD5Encryption(password, salt);
        System.out.println("md5+随机盐："+md5Encryption);
        String md5Encryption1 = Md5SaltUtils.getMd5Encryption(password);
        System.out.println("MD5+固定盐值："+md5Encryption1);
    }

    @Test
    void getMD5Encryption() {
    }

    @Test
    void getMd5Encryption() {
    }

    @Test
    void getSalt() {
    }
}