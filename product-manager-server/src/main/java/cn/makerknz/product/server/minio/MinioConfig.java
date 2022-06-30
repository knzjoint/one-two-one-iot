package cn.makerknz.product.server.minio;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
  * @className: MinioConfig
  * @author Hope
  * @date 2020/7/28 13:43 
  * @description: MinioConfig
  */

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private String endpoint;

    private int port;

    private String accessKey;

    private String secretKey;

    private Boolean secure;

    private String bucketName;

    private String configDir;

    @Bean
    public MinioClient getMinioClient() {
        // MinioClient minioClient = new MinioClient(endpoint, port, accessKey, secretKey, secure);
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint, port, secure)
                .credentials(accessKey, secretKey)
                .build();
        return minioClient;
    }
}