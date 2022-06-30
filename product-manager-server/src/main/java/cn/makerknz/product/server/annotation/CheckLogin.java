package cn.makerknz.product.server.annotation;

import java.lang.annotation.*;

/**
 * 判断是否登录的注解，用户认证切面
 * @Author: maker_knz
 * @Date: 2021/5/31/031 17:59
 * @Version 1.0
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface CheckLogin {
}
