package vip.yeee.memoo.common.dingtalk.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/6/20 17:33
 */
@ConfigurationProperties(prefix = "yeee.dingtalk")
@Configuration
@Data
public class DingtalkProperty {

    private Map<String, String> chatGroup;
}
