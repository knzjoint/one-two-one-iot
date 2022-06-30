package cn.makerknz.product.server.auth;

import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.utils.ApplicationTokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ApplicationAuthAspect {

    final static private String APPLICATION_AUTH_TOKEN = "ApplicationToken";

    @Autowired
    private ApplicationTokenUtils applicationTokenUtils;


    /**
     * 切面检查权限问题
     * @param point
     * @return
     */
    @Around("@annotation(cn.makerknz.product.server.annotation.CheckThirdApplicationAccess)")
    public Object checkAuth(ProceedingJoinPoint point) {
        try {
            // 1.验证token是否合法
            HttpServletRequest request = this.getHttpServletRequest();
            // 1.从header中获取token
            String token = request.getHeader(APPLICATION_AUTH_TOKEN);
            // 2.校验token是否合法
            this.setApplicationInfo(request, token);

            return point.proceed();
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
            throw new BusinessException(ExceptionEnum.APPLICATION_UNAUTHORIZED);
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

    /**
     * 在请求头中获取用户信息存储在claims中，这些信息是用来权限认证的基础
     * @param request
     * @param token
     */
    private void setApplicationInfo(HttpServletRequest request, String token) {
        Claims claims = applicationTokenUtils.getClaimsFromToken(token);
        request.setAttribute("id", claims.get("id"));
        request.setAttribute("nickname", claims.get("nickname"));
        request.setAttribute("role", claims.get("role"));
    }

}
