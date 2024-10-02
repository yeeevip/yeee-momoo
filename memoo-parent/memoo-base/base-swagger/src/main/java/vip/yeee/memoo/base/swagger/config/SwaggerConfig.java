package vip.yeee.memoo.base.swagger.config;

import jakarta.annotation.Resource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger API文档相关配置
 */
@Configuration
@EnableConfigurationProperties({SwaggerProperties.class})
public class SwaggerConfig extends BaseSwaggerConfig {

    @Resource
    private SwaggerProperties swaggerProperties;

    @Override
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties()
                .setEnable(swaggerProperties.isEnable())
                .setApiBasePackage(swaggerProperties.getApiBasePackage())
                .setTitle(swaggerProperties.getTitle())
                .setDescription(swaggerProperties.getDescription())
                .setContactName(swaggerProperties.getContactName())
                .setVersion("1.0")
                .setEnableSecurity(true);
    }

}
