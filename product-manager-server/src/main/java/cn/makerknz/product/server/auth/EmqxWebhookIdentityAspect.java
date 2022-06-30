package cn.makerknz.product.server.auth;

import cn.makerknz.product.server.config.EmqxConfig;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: maker_knz
 * @Date: 2021/6/1/001 10:05
 * @Version 1.0
 */

@Aspect
@Component
public class EmqxWebhookIdentityAspect {

//    @Value("#{${emqx.webhook.users}}")
//    private Map<String, String> webhookUsers;

    @Autowired
    private EmqxConfig emqxConfig;

    /**
     * 切面验证webhook接口是否可以访问
     * @param point
     * @return
     */
    @Around("@annotation(cn.makerknz.product.server.annotation.CheckEmqxWebhookIdentity)")
    public Object checkLogin(ProceedingJoinPoint point) {

        try {
            HttpServletRequest request = this.getHttpServletRequest();

            // 1.从header中获取username和passowrd
            String username = request.getHeader("webhook-username");
            String password = request.getHeader("webhook-password");

            String userPassword = emqxConfig.getUsers().get(username);
            if (!userPassword.equals(password)) {
                throw new BusinessException(ExceptionEnum.APPLICATION_WEBHOOK_AUTHORIZED_ERROR);
            }

            return point.proceed();
        } catch (Throwable throwable) {
            throw new BusinessException(ExceptionEnum.APPLICATION_WEBHOOK_SETTING_ERROR);
        }
    }

    /**
     * 从请求头中获取HttpServletRequest
     * @return
     */
    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return servletRequestAttributes.getRequest();
    }

}
