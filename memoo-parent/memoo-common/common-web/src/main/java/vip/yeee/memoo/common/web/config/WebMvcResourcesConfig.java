package vip.yeee.memoo.common.web.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.yeee.memoo.common.web.filter.FilterDemo;
import vip.yeee.memoo.common.web.interceptor.InterceptorDemo;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 资源配置类
 *
 * @author https://www.yeee.vip
 * @since 2022/7/12 14:16
 */
@Configuration
public class WebMvcResourcesConfig implements WebMvcConfigurer {

    @Resource
    private InterceptorDemo interceptorDemo;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptorDemo).addPathPatterns("/common/test/**");
    }

    @Bean
    public FilterRegistrationBean<FilterDemo> filterDemo() {
        FilterRegistrationBean<FilterDemo> registrationBean = new FilterRegistrationBean<>();
        // 注册自定义的 Filter
        registrationBean.setFilter(new FilterDemo());
        // 设置拦截Url映射
        registrationBean.setUrlPatterns(new ArrayList<>(Collections.singletonList("/common/test/*")));
        return registrationBean;
    }

}
