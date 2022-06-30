package cn.makerknz.product.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/25 8:03
 */

@Data
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jwt")
@Configuration
public class JwtTokenConfig {

    /**
     * 秘钥
     * - 默认maker_knz_kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk
     */
    @Value("${secret:maker_knz_kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk}")
    private String secret;

    /**
     * 有效期，单位秒
     * - 默认2周
     */
    @Value("${expire-time-in-second:1209600}")
    private Long expirationTimeInSecond;

}
