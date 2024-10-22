package vip.yeee.memoo.demo.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import vip.yeee.memoo.demo.springboot.extpoint.Ext01ApplicationContextInitializer;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/5/18 9:30
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
public class SpringbootExampleApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SpringbootExampleApplication.class);
        springApplication.addInitializers(new Ext01ApplicationContextInitializer());
        ConfigurableApplicationContext applicationContext = springApplication.run(args);
    }
}
