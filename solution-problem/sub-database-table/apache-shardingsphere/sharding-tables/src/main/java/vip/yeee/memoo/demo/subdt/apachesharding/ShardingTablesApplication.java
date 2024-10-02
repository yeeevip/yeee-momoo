package vip.yeee.memoo.demo.subdt.apachesharding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/6/22 15:14
 */
@MapperScan("vip.yeee.memoo.demo.subdt.apachesharding.mapper")
@SpringBootApplication
public class ShardingTablesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingTablesApplication.class, args);
    }

}
