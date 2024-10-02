package vip.yeee.memoo.demo.websocket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("vip.yeee.memoo.demo.websocket.mapper")
@SpringBootApplication
public class WebsocketDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketDemoApplication.class, args);
	}

}
