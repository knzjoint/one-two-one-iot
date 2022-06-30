package cn.makerknz.product.server.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 朱康南
 * @Date: 2019/12/28/028 13:34
 * @Version 1.0
 */
@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "one-two-one.sms.ali-yun-sms")
@Data
public class SmsConfig {

    /**
     * 通过配置打开sms发送验证码
     */
    private Boolean enabled;

    /**
     * 访问ID
     */
    private String accessKeyId;

    /**
     * 访问密钥
     */
    private String secret;

    /**
     * 地区ID
     */
    private String regionId = "cn-shanghai";

    /**
     * code格式化， 设置在模板的样式参数里
     */
    private String codeFormat = "{\"code\":\"%s\"}";

    /**
     * 访问的域名
     */
    private String domain = "dysmsapi.aliyuncs.com";

    /**
     * 版本
     */
    private String version = "2017-05-25";

    /**
     * 行为
     */
    private String action = "SendSms";

    /**
     * 签名
     */
    private String signName = "makerKnz";


    @Bean
    public Client aliyunSmsCode() throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(secret);
        // 访问的域名
        config.endpoint = domain;
        return new Client(config);
    }


}
