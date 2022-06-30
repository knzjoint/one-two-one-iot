package cn.makerknz.product.server.annotation;

import java.lang.annotation.*;

/**
 * 第三方访服务接口时需要认证，通过该注解进行切面
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/5 15:52
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface CheckThirdApplicationAccess {
}
