package vip.yeee.memoo.base.web.config;

import cn.hutool.core.collection.CollectionUtil;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.yeee.memoo.base.web.properties.BaseWebProperty;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/6/22 18:55
 */
@Configuration
public class BaseWebMvcConfigurer implements WebMvcConfigurer {

    @Resource
    private BaseWebProperty baseWebProperty;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (baseWebProperty == null || baseWebProperty.getCors() == null || !baseWebProperty.getCors().getEnable()) {
            return;
        }
        if (CollectionUtil.isEmpty(baseWebProperty.getCors().getMappings())) {
            registry.addMapping("/**")
                    .allowedOriginPatterns("*")
                    .allowedMethods("*")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);
        } else {
            for (String mapping : baseWebProperty.getCors().getMappings()) {
                registry.addMapping(mapping)
                        .allowedOriginPatterns("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        }

    }

}
