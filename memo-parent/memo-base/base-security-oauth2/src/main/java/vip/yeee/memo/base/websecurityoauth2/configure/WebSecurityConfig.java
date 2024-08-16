package vip.yeee.memo.base.websecurityoauth2.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import vip.yeee.memo.base.websecurityoauth2.constant.AuthConstant;

/**
 * SpringSecurity配置类
 *
 * @author https://www.yeee.vip
 * @since 2022/4/28 16:14
 */
//@Order(Integer.MIN_VALUE)
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
        return new Md5PasswordEncoder();
    }

    @Order(1)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    // 禁用basic明文验证
                    .httpBasic().disable()
                    // 前后端分离架构不需要csrf保护
                    .csrf().disable()
                    // 禁用默认登录页
                    .formLogin().disable()
                    // 禁用默认登出页
                    .logout().disable()
                    // 前后端分离是无状态的，不需要session了，直接禁用。
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                            // 允许所有OPTIONS请求
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            // 匿名访问
                            .requestMatchers(AuthConstant.BASE_EXCLUDE_PATTERNS).permitAll()
                            // 允许任意请求被已登录用户访问，不检查Authority
                            .anyRequest().authenticated())
                    ;
            return http.build();
    }

}
