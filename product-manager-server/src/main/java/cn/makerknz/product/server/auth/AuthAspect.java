package cn.makerknz.product.server.auth;

import cn.makerknz.product.server.annotation.CheckAuthorization;
import cn.makerknz.product.server.domain.enums.RoleEnum;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.utils.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: maker_knz
 * @Date: 2021/6/1/001 10:05
 * @Version 1.0
 */

@Aspect
@Component
@Slf4j
public class AuthAspect {

    List<String> sysRoles;

    /**
     * 根据之前数据库角色生成，默认
     */
    public AuthAspect() {
        sysRoles = RoleEnum.toList();
    }

    /**
     * 切面检查权限问题
     * @param point
     * @return
     */
    @Around("@annotation(cn.makerknz.product.server.annotation.CheckAuthorization)")
    public Object checkAuth(ProceedingJoinPoint point) {
        try {
            // 1.验证token是否合法
            HttpServletRequest request = this.getHttpServletRequest();
            String token = this.getToken(request);
            // 2.验证角色是否匹配
            this.setUserInfo(request, token);
            String role = (String) request.getAttribute("role");
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            CheckAuthorization annotation = method.getAnnotation(CheckAuthorization.class);
            String value = annotation.value();

            if (!sysRoles.contains(role)) {
                throw new BusinessException(ExceptionEnum.UNAUTHORIZED, "用户无权访问!");
            }
            return point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BusinessException(ExceptionEnum.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 切面验证用户是否登录
     * @param point
     * @return
     */
    @Around("@annotation(cn.makerknz.product.server.annotation.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint point) {

        try {
            HttpServletRequest request = this.getHttpServletRequest();
            String token = this.getToken(request);
            // 如果检验成功将用户信息设置到request的atribute里面
            this.setUserInfo(request, token);

            return point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BusinessException(ExceptionEnum.INTERNAL_SERVER_ERROR);
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
     * 从请求头中获取Token
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request) {
        // 1.从header中获取token
        String token = request.getHeader("accessToken");
        // 2.校验token是否合法
        Boolean isValid = JwtTokenUtils.validateToken(token);
        if (!isValid) {
            throw new BusinessException(ExceptionEnum.UNAUTHORIZED, "Token不合法!");
        }
        return token;
    }

    /**
     * 在请求头中获取用户信息存储在claims中，这些信息是用来权限认证的基础
     * @param request
     * @param token
     */
    private void setUserInfo(HttpServletRequest request, String token) {
        Claims claims = JwtTokenUtils.getClaimsFromToken(token);
        request.setAttribute("id", claims.get("id"));
        request.setAttribute("nickname", claims.get("nickname"));
        request.setAttribute("role", claims.get("role"));
    }

}
