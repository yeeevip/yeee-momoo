package vip.yeee.memoo.common.scloud.gray.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/12/6 15:04
 */
@Slf4j
@ComponentScan("vip.yeee.memoo.common.scloud.gray.gateway")
public class CommonSpringCloudGrayGatewayAutoConfigure {
    public CommonSpringCloudGrayGatewayAutoConfigure() {
        log.info("自动配置-CommonSpringCloudGrayGatewayAutoConfigure");
    }
}
