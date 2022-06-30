package cn.makerknz.product.server.annotation;

import java.lang.annotation.*;

/**
 * 检查是否有访问权限的注解，该注解仅可以使用在方法上
 * @Author: maker_knz
 * @Date: 2021/6/1/001 10:03
 * @Version 1.0
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface CheckAuthorization {

    // 角色的名称
    String value();

}
