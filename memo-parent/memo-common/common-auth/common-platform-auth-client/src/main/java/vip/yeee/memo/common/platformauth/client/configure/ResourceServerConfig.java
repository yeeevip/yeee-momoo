package vip.yeee.memo.common.platformauth.client.configure;

import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Sets;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import vip.yeee.memo.base.model.annotation.AnonymousAccess;
import vip.yeee.memo.base.util.LogUtils;
import vip.yeee.memo.base.websecurityoauth2.constant.AuthConstant;
import vip.yeee.memo.common.platformauth.client.properties.AuthClientProperties;

import java.io.IOException;
import java.util.*;

/**
 * 资源服务器配置
 *
 * @author https://www.yeee.vip
 * @since 2022/4/28 17:02
 */
@Configuration
public class ResourceServerConfig{

    private final static Logger log = LogUtils.commonAuthLog();
    @Resource
    private AuthClientProperties authClientProperties;
    @Resource
    private ApplicationContext applicationContext;
    private static final Set<String> anonymousUrls = Sets.newHashSet();

    @PostConstruct
    public void init() {
        authClientProperties.getExclude().addAll(Arrays.asList(AuthConstant.BASE_EXCLUDE_PATTERNS));
        authClientProperties.getExclude().addAll(authClientProperties.getExclude());
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        handlerMethods.forEach((k, v) -> {
            AnonymousAccess anonymousAccess = v.getMethodAnnotation(AnonymousAccess.class);
            // spring.mvc.pathmatch.matching-strategy: ant_path_matcher
            if (k.getPatternsCondition() != null && anonymousAccess != null && anonymousAccess.valid()) {
                authClientProperties.getExclude().addAll(k.getPatternsCondition().getPatterns().stream().toList());
            }
//            if (k.getPathPatternsCondition() != null && anonymousAccess != null && anonymousAccess.valid()) {
//                anonymousUrls.addAll(k.getPathPatternsCondition().getPatterns().stream().map(PathPattern::getPatternString).collect(Collectors.toList()));
//            }
        });
        anonymousUrls.addAll(authClientProperties.getExclude());
    }

    @Order(2)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
//                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .requestMatchers(ArrayUtil.toArray(anonymousUrls, String.class)).permitAll()
                // 内部调用
                .requestMatchers("/server/**").permitAll()
//                .antMatchers("/**/front/**").hasAnyAuthority(SecurityUserTypeEnum.SYSTEM_USER.getRole())
//                .antMatchers("/**/system/**").hasAnyAuthority(SecurityUserTypeEnum.FRONT_USER.getRole())
//                .antMatchers("/**/front/**").hasRole(SecurityUserTypeEnum.FRONT_USER.getRole())
//                .antMatchers("/**/system/**").hasRole(SecurityUserTypeEnum.SYSTEM_USER.getRole())
                .anyRequest().authenticated();
        ;
        http.addFilterBefore(new AuthenticationBeforeFilter(), AbstractPreAuthenticatedProcessingFilter.class);
        return http.build();
    }

    private static class AuthenticationBeforeFilter extends OncePerRequestFilter {

        @Override
        public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws IOException, ServletException {

            HttpServletRequest finalRequest = request;
            if (anonymousUrls.contains(request.getRequestURI())
                    || anonymousUrls.stream().anyMatch(l -> new AntPathMatcher().match(l, finalRequest.getRequestURI()))) {
                log.debug("[YEEE认证] - 包含匿名请求，url = {}", request.getRequestURI());
                // 在此处创建HttpServletRequestWrapper对象并包装原始请求
                request = new HttpServletRequestWrapper(request) {
                    @Override
                    public String getHeader(String name) {
                        if (AuthConstant.JWT_TOKEN_HEADER.equalsIgnoreCase(name)) {
                            return null;
                        }
                        return super.getHeader(name);
                    }

                    @Override
                    public Enumeration<String> getHeaders(String name) {
                        if (AuthConstant.JWT_TOKEN_HEADER.equalsIgnoreCase(name)) {
                            return Collections.emptyEnumeration();
                        }
                        return super.getHeaders(name);
                    }

                    @Override
                    public Enumeration<String> getHeaderNames() {
                        List<String> headerNames = Collections.list(super.getHeaderNames());
                        headerNames.remove(AuthConstant.JWT_TOKEN_HEADER);
                        return Collections.enumeration(headerNames);
                    }
                };
            }
            filterChain.doFilter(request, response);
        }
    }

}
