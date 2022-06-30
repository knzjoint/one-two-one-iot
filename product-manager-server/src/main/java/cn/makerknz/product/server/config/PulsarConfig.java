package cn.makerknz.product.server.config;

import lombok.Data;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: maker_knz
 * @Date: 2022/2/28/028 15:03
 * @Version 1.0
 */

@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "pulsar")
@Data
public class PulsarConfig {

    private String server;

    private String privateToken;

    /**
     * pulsar接口
     */
    private String url;

    /**
     * pulsar认证服务
     */
    private String authPluginClassName;

    /**
     * 认证参数，token:jwt
     */
    private String authParams;

    /**
     * PulsarAdmin对象
     * @return
     * @throws PulsarClientException
     */
    @Bean
    public PulsarAdmin pulsarAdmin() throws PulsarClientException {
        boolean useTls = false;
        boolean tlsAllowInsecureConnection = false;
        String tlsTrustCertsFilePath = null;
        PulsarAdmin admin = PulsarAdmin.builder()
                .authentication(authPluginClassName, authParams)
                .serviceHttpUrl(url)
                .tlsTrustCertsFilePath(tlsTrustCertsFilePath)
                .allowTlsInsecureConnection(tlsAllowInsecureConnection)
                .build();

        return admin;
    }

}
