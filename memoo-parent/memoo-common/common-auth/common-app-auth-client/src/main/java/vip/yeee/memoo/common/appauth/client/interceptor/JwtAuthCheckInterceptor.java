package vip.yeee.memoo.common.appauth.client.interceptor;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import vip.yeee.memoo.base.model.annotation.AnonymousAccess;
import vip.yeee.memoo.base.model.exception.BizException;
import vip.yeee.memoo.base.model.rest.ResultCode;
import vip.yeee.memoo.base.util.LogUtils;
import vip.yeee.memoo.common.appauth.client.constant.ApiAuthConstant;
import vip.yeee.memoo.common.appauth.client.context.ApiSecurityContext;
import vip.yeee.memoo.common.appauth.client.model.ApiAuthedUser;
import vip.yeee.memoo.common.appauth.client.kit.JwsClientKit;
import vip.yeee.memoo.common.appauth.client.kit.JwtClientKit;
import vip.yeee.memoo.base.util.JacksonUtils;
import vip.yeee.memoo.common.appauth.client.properties.ApiAuthClientProperties;


/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/16 14:38
 */
@Component
@ConditionalOnClass(org.springframework.web.servlet.HandlerInterceptor.class)
public class JwtAuthCheckInterceptor implements HandlerInterceptor {

    private final static Logger log = LogUtils.commonAuthLog();
    @Resource
    private JwtClientKit jwtClientKit;
    @Resource
    private JwsClientKit jwsClientKit;
    @Resource
    private ApiAuthClientProperties apiAuthClientProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AnonymousAccess annotation = handlerMethod.getMethod().getAnnotation(AnonymousAccess.class);
            if (annotation != null) {
                log.debug("【API资源拦截器】- 匿名资源 - url = {}", request.getRequestURI());
                return true;
            }
        }
        log.debug("【API资源拦截器】- url = {}", request.getRequestURI());
        String token = request.getHeader(ApiAuthConstant.TOKEN);
        boolean success = true;
        if (StrUtil.isBlank(token) || StrUtil.isBlank(token = token.replace(ApiAuthConstant.JWT_TOKEN_PREFIX, ""))) {
            success = false;
        } else {
            try {
                Claims claims = jwtClientKit.getTokenClaim(token);
                if (claims != null) {
                    ApiAuthedUser authedUser = JacksonUtils.toJavaBean(claims.getSubject(), ApiAuthedUser.class);
                    ApiSecurityContext.curUser.set(authedUser);
                }
//            PayloadDto payloadDto = jwsClientKit.verifyTokenByRSA(token);
//            ApiAuthedUser authedUser = JacksonUtils.toJavaBean(payloadDto.getSub(), ApiAuthedUser.class);
//            ApiSecurityContext.curUser.set(authedUser);
            } catch (BizException e) {
                success = false;
            }
        }
        if (!success) {
            if (StrUtil.isNotBlank(apiAuthClientProperties.getFailUrl())) {
                response.sendRedirect(apiAuthClientProperties.getFailUrl());
            } else {
                throw new BizException(ResultCode.UNAUTHORIZED);
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ApiSecurityContext.curUser.remove();
    }
}
