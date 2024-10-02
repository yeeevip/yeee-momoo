package vip.yeee.memoo.base.swagger.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

/**
 * Swagger基础配置
 */
public abstract class BaseSwaggerConfig {

    @Value("${spring.security.oauth2.authorizationserver.token-uri}")
    private String tokenUrl;

    @Bean
    public OpenAPI createRestApi() {
        SwaggerProperties swaggerProperties = swaggerProperties();
        Contact contact = new Contact();
        contact.setName(swaggerProperties.getContactName());
        contact.setEmail(swaggerProperties.getContactEmail());
        contact.setUrl(swaggerProperties.getContactUrl());
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(HttpHeaders.AUTHORIZATION,
                                new SecurityScheme()
                                        // OAuth2 授权模式
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .name(HttpHeaders.AUTHORIZATION)
                                        .flows(new OAuthFlows()
                                                .password(
                                                        new OAuthFlow()
                                                                .tokenUrl(tokenUrl)
                                                                .refreshUrl(tokenUrl)
                                                )
                                        )
                                        // 安全模式使用Bearer令牌（即JWT）
                                        .in(SecurityScheme.In.HEADER)
                                        .scheme("Bearer")
                                        .bearerFormat("JWT")
                        )
                )
                // 接口全局添加 Authorization 参数
                .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
                // 接口信息定义
                .info(new Info().title(swaggerProperties.getTitle())
                        .contact(contact)
                        .description(swaggerProperties.getDescription())
                        .version(swaggerProperties.getVersion())
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description(swaggerProperties.getDescription())
                        .url(swaggerProperties.getContactUrl()));
    }

    /**
     * 自定义Swagger配置
     */
    public abstract SwaggerProperties swaggerProperties();
}
