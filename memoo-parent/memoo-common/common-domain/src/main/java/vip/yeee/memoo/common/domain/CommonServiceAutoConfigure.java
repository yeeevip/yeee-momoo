package vip.yeee.memoo.common.domain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/7/12 11:49
 */
@ComponentScan("vip.yeee.memoo.common.domain")
@MapperScan("vip.yeee.memoo.common.domain.mapper")
public class CommonServiceAutoConfigure {
}
