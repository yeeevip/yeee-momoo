package vip.yeee.memoo.base.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/6/22 18:56
 */
@ConfigurationProperties(prefix = "yeee.web")
@Component
@Data
public class BaseWebProperty {

    private Cors cors;

    @Data
    public static class Cors {

        private Boolean enable = false;

        private List<String> mappings;
    }
}
