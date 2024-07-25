package vip.yeee.memo.common.platformauth.server.configure;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.annotation.Resource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import vip.yeee.memo.common.platformauth.server.constant.SecurityConstants;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 认证服务器配置
 *
 * @author https://www.yeee.vip
 * @since 2022/4/28 16:25
 */
@Configuration
public class Oauth2ServerAutoConfiguration {

    @Resource
    private DataSource dataSource;
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private TokenStore tokenStore;
    @Resource
    private AccessTokenConverter accessTokenConverter;

    // 从数据库中查询出客户端信息
    @Bean
    public JdbcClientDetailsService clientDetailsService() {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
        clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
        return clientDetailsService;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //从数据库取数据
        clients.withClientDetails(clientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 允许form表单客户端认证,允许客户端使用client_id和client_secret获取token
                .allowFormAuthenticationForClients()
                // 通过验证返回token信息
                .checkTokenAccess("isAuthenticated()")
                // 获取token请求不进行拦截
                .tokenKeyAccess("permitAll()")
                //获取密钥需要身份认证,使用单点登录时必须配置
//                .tokenKeyAccess("isAuthenticated()")
        ;

    }

    // 使用密码模式所需配置
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

//        // 配置JWT的内容增强器
//        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
//        List<TokenEnhancer> delegates = new ArrayList<>();
//        delegates.add(jwtTokenEnhancer);
//        delegates.add((TokenEnhancer) accessTokenConverter);
//        enhancerChain.setTokenEnhancers(delegates);

        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenServices(customTokenService(tokenStore, clientDetailsService()))
                //配置存储令牌策略
                .tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter)
//                .tokenEnhancer(enhancerChain)
        ;
    }

    @Bean
    public CustomTokenService customTokenService(TokenStore tokenStore, ClientDetailsService clientDetailsService) {
        CustomTokenService customTokenService = new CustomTokenService();
        customTokenService.setTokenStore(tokenStore);
        customTokenService.setClientDetailsService(clientDetailsService);
        return customTokenService;
    }

    /**
     * check_token端点重写
     */
    @Bean
    public CheckTokenEndpoint checkTokenEndpoint(CustomTokenService customTokenService) {
        CheckTokenEndpoint checkTokenEndpoint = new CheckTokenEndpoint(customTokenService);

        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
        UserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter() {
            @Override
            public Map<String, ?> convertUserAuthentication(Authentication authentication) {
                Map<String, Object> response = new LinkedHashMap<>();
                response.put(USERNAME, authentication.getPrincipal());
                if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
                    response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
                }
                return response;
            }

        };
        defaultAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter);

        checkTokenEndpoint.setAccessTokenConverter(defaultAccessTokenConverter);
//        checkTokenEndpoint.setExceptionTranslator(cloudWebResponseExceptionTranslator);
        return checkTokenEndpoint;
    }

    @ConfigurationProperties(prefix = "security.oauth2.client")
	@Bean
	public OAuth2ProtectedResourceDetails resourceOwnerPasswordResourceDetails() {
		return new ResourceOwnerPasswordResourceDetails();
	}











    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());	// Enable OpenID Connect 1.0
        http
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/login"))
                )
                // Accept access tokens for User Info and/or Client Registration
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/actuator/**"),
                                new AntPathRequestMatcher("/oauth2/**"),
                                new AntPathRequestMatcher("/**/*.json"),
                                new AntPathRequestMatcher("/**/*.html")).permitAll()
                        .anyRequest().authenticated()
                )
                // Form login handles the redirect to the login page from the
                // authorization server filter chain
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }


    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

}
