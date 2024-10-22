package vip.yeee.memoo.common.encrypt.mybatis.config;

import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.yeee.memoo.common.encrypt.mybatis.Encrypt;
import vip.yeee.memoo.common.encrypt.mybatis.encrypt.AesSupport;
import vip.yeee.memoo.common.encrypt.mybatis.interceptor.DecryptReadInterceptor;
import vip.yeee.memoo.common.encrypt.mybatis.interceptor.SensitiveAndEncryptWriteInterceptor;
import vip.yeee.memoo.common.encrypt.mybatis.properties.MybatisEncryptProperty;

/**
 * 插件配置
 */
@Configuration
public class EncryptPluginConfig {

    @Resource
    private MybatisEncryptProperty mybatisEncryptProperty;

    @Bean
    Encrypt encryptor() throws Exception{ ;
       return new AesSupport(mybatisEncryptProperty.getPassword());
    }

    @ConditionalOnClass(org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer.class)
    @Configuration
    public class MybatisEncryptPluginConfig {

        @Bean
        org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer mybatisConfigurationCustomizer() throws Exception{
            DecryptReadInterceptor decryptReadInterceptor = new DecryptReadInterceptor(encryptor());
            SensitiveAndEncryptWriteInterceptor sensitiveAndEncryptWriteInterceptor = new SensitiveAndEncryptWriteInterceptor(encryptor());

            return (configuration) -> {
                configuration.addInterceptor(decryptReadInterceptor);
                configuration.addInterceptor(sensitiveAndEncryptWriteInterceptor);
            };
        }
    }

    @ConditionalOnClass(com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer.class)
    @Configuration
    public class MybatisPlusEncryptPluginConfig {

        @Bean
        com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer mybatisPlusConfigurationCustomizer() throws Exception{
            DecryptReadInterceptor decryptReadInterceptor = new DecryptReadInterceptor(encryptor());
            SensitiveAndEncryptWriteInterceptor sensitiveAndEncryptWriteInterceptor = new SensitiveAndEncryptWriteInterceptor(encryptor());

            return (configuration) -> {
                configuration.addInterceptor(decryptReadInterceptor);
                configuration.addInterceptor(sensitiveAndEncryptWriteInterceptor);
            };
        }
    }

}
