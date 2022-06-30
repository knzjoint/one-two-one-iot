package cn.makerknz.product.server.annotation;

import java.lang.annotation.*;

/**
 * EMQX使用webhook时做安全认证，通过切面验证请求头中的内容，
 * 配置在EMQX的EMQX/etc/plugins/emqx_web_hook.conf
 * web.hook.headers.webhook-username = makerknz
 * web.hook.headers.webhook-password = 123456
 * 该注解仅可以使用在方法上
 * @Author: maker_knz
 * @Date: 2021/6/1/001 10:03
 * @Version 1.0
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface CheckEmqxWebhookIdentity {
}
