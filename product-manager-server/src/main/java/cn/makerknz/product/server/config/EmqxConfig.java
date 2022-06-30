package cn.makerknz.product.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @Author: maker_knz
 * @Date: 2021/6/1/001 10:05
 * @Version 1.0
 */

@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "emqx.webhook")
@Data
public class EmqxConfig {

    private Map<String,String> users;

}
