package cn.makerknz.product.server;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;

/**
 * @Author: maker_knz
 * @Date: 2022/3/8/008 11:39
 * @Version 1.0
 */

@SpringBootTest
public class KeyPairGenerator {

    public static void main(String[] args) throws IOException {
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);

        FileOutputStream out = new FileOutputStream("C:\\Users\\asus\\Desktop\\out.txt");

        // 定义一个字符串
        String str="GouHuiPeng";
        out.write(keyPair.getPrivate().getEncoded());

        //关闭流
        out.close();

    }

}
