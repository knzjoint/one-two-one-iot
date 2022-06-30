package cn.makerknz.product.server.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.shade.org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

/**
 * @Author: maker_knz
 * @Date: 2022/3/7/007 14:10
 * @Version 1.0
 */

@Component
@Slf4j
public class ApplicationTokenUtils {

    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    static {
        try {
            privateKey = generatePrivateKey(getPrivateKeyString());
            publicKey = generatePublicKey(getPublicKeyString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] getPrivateKeyString() throws IOException {
        File file = ResourceUtils.getFile("classpath:static/pulsar-jwt/jwt-private.key");
        InputStream inputStream = new FileInputStream(file);

        //获取本地私钥文件*.pem
        return IOUtils.toByteArray(inputStream);
    }

    private static byte[] getPublicKeyString() throws IOException {
        File file = ResourceUtils.getFile("classpath:static/pulsar-jwt/jwt-public.key");
        InputStream inputStream = new FileInputStream(file);

        //获取本地私钥文件*.pem
        return IOUtils.toByteArray(inputStream);
    }

    private static PrivateKey generatePrivateKey(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec keySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }


    private static PublicKey generatePublicKey(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * jjwt生成jwt,RS256加密
     *
     * @return
     * @throws IOException
     */
    public String generateToken(Map<String, Object> map, String subject) throws IOException {
        String token = Jwts
                .builder()
                .setSubject(subject)
                .addClaims(map)
//                .setExpiration(new Date(now.getTime() + 3600 * 1000))
//                .setIssuer(ConstantUtil.ISS)
//                .setIssuedAt(now)
//                .setAudience(ConstantUtil.AUD)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
        return token;
    }

    public Claims getClaimsFromToken(String token) {

        try {
            return Jwts.parser()
                    .setSigningKey(this.publicKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            log.error("token解析错误", e);
            throw new IllegalArgumentException("Token invalided.");
        }
    }


}
