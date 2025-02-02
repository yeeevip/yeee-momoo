package vip.yeee.memoo.demo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import vip.yeee.memoo.demo.kafka.producer.KafkaProducer;

import jakarta.annotation.Resource;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/13 15:32
 */
@Slf4j
@SpringBootTest
public class KafkaTests {

    @Resource
    private KafkaProducer kafkaProducer;

    @Test
    public void testSendMessage() {
        kafkaProducer.sendMessage("TP_memo-test", "测试发送消息！！！");
    }

    @Test
    public void testSendTransactionMessage() {
        kafkaProducer.sendTransactionMessage("TP_memo-test", "测试发送消息！！！", v -> {
            log.info("----------------消息发送后执行其他操作-----------------");
            throw new RuntimeException();
        });
    }

}
