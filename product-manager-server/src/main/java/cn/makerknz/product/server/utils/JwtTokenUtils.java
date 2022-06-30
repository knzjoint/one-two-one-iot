package cn.makerknz.product.server.utils;

import cn.makerknz.product.server.config.JwtTokenConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类
 * @Author: maker_knz
 * @Date: 2021/5/31/031 16:49
 * @Version 1.0
 */

@Component
@Slf4j
public class JwtTokenUtils implements ApplicationContextAware {

    static private JwtTokenConfig jwtTokenConfig;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JwtTokenUtils.jwtTokenConfig = (JwtTokenConfig) applicationContext.getBean("jwtTokenConfig");
    }

    /**
     * 从token中获取claim
     *
     * @param token token
     * @return claim
     */
    static public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(JwtTokenUtils.jwtTokenConfig.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            log.error("token解析错误", e);
            throw new IllegalArgumentException("Token invalided.");
        }
    }

    /**
     * 获取token的过期时间
     *
     * @param token token
     * @return 过期时间
     */
    static public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token)
                .getExpiration();
    }

    /**
     * 判断token是否过期
     *
     * @param token token
     * @return 已过期返回true，未过期返回false
     */
    static private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 计算token的过期时间
     *
     * @return 过期时间
     */
    static public Date getExpirationTime() {
        return new Date(System.currentTimeMillis() + JwtTokenUtils.jwtTokenConfig.getExpirationTimeInSecond() * 1000);
    }

    /**
     * 为指定用户生成token
     *
     * @param claims 用户信息
     * @return token
     */
    static public String generateToken(Map<String, Object> claims) {
        Date createdTime = new Date();
        Date expirationTime = JwtTokenUtils.getExpirationTime();


        byte[] keyBytes = JwtTokenUtils.jwtTokenConfig.getSecret().getBytes();
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(createdTime)
                .setExpiration(expirationTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 判断token是否非法
     *
     * @param token token
     * @return 未过期返回true，否则返回false
     */
    static public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

}
