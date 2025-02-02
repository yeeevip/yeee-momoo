package vip.yeee.memoo.common.platformauth.client.interceptor;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import vip.yeee.memoo.base.util.JacksonUtils;
import vip.yeee.memoo.base.util.LogUtils;
import vip.yeee.memoo.base.websecurityoauth2.constant.AuthConstant;
import vip.yeee.memoo.base.websecurityoauth2.context.SecurityContext;
import vip.yeee.memoo.base.websecurityoauth2.model.AuthedUser;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/16 14:38
 */
@Component
public class SecurityTokenInterceptor implements HandlerInterceptor {

    private final static Logger log = LogUtils.commonAuthLog();
//    @Resource
//    private AuthProperties authProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //            Jws<Claims> claimsJws = Jwts.parser()
//                    .setSigningKey(authProperties.getJwtSecret().getBytes(StandardCharsets.UTF_8))
//                    .parseClaimsJws(token);
//            String subject = claimsJws.getBody().getSubject();
//            JacksonUtils.toJavaBean(subject, SecurityUser.class)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return true;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            return true;
        }
        AuthedUser authedUser = JacksonUtils.toJavaBean(JacksonUtils.toJsonString(principal), AuthedUser.class);
        Map<Boolean, List<String>> map = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.groupingBy(s -> s.startsWith(AuthConstant.ROLE_PREFIX)));
        authedUser.setPermissions(Sets.newHashSet(Optional.ofNullable(map.get(Boolean.FALSE)).orElseGet(Lists::newArrayList)));
        authedUser.setRoles(Sets.newHashSet(Optional.ofNullable(map.get(Boolean.TRUE)).orElseGet(Lists::newArrayList)));
        SecurityContext.curUser.set(authedUser);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SecurityContext.curUser.remove();
    }
}
