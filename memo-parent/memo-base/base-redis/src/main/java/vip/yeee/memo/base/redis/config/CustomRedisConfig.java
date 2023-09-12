package vip.yeee.memo.base.redis.config;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import vip.yeee.memo.base.redis.properties.CustomRedisProperty;

import javax.annotation.Resource;


/**
 * 自定义数据源Redis配置
 */
@Configuration
@ConditionalOnProperty(value = "yeee.custom.redis.enabled", havingValue = "true")
@ConditionalOnBean(CustomRedisProperty.class)
public class CustomRedisConfig implements InitializingBean, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(CustomRedisConfig.class);

    private ApplicationContext applicationContext;

    @Resource
    private CustomRedisProperty customRedisProperty;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (CollectionUtil.isEmpty(customRedisProperty.getInstance())) {
            return;
        }

        log.info("自定义Redis数据源 - 开始");

        for (CustomRedisProperty.CustomRedisProperties customRedisProperties : customRedisProperty.getInstance()) {

            String beanName = customRedisProperties.getBeanName();

            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration ();
            redisStandaloneConfiguration.setHostName(customRedisProperties.getHost());
            redisStandaloneConfiguration.setPort(customRedisProperties.getPort());
            redisStandaloneConfiguration.setDatabase(customRedisProperties.getDatabase());
            redisStandaloneConfiguration.setPassword(customRedisProperties.getPassword());
            LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
            lettuceConnectionFactory.afterPropertiesSet(); // 初始化连接工厂

            //设置序列化
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
            jackson2JsonRedisSerializer.setObjectMapper(om);

            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(lettuceConnectionFactory);
            template.setKeySerializer(new StringRedisSerializer());
            template.setValueSerializer(jackson2JsonRedisSerializer);

            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(RedisTemplate.class);
            beanDefinition.setInstanceSupplier(() -> template);

            ((GenericApplicationContext)applicationContext).registerBeanDefinition(beanName, beanDefinition);

            log.info("自定义Redis数据源 - {} - completed！！！", beanName);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
