package vip.yeee.memoo.demo.jasypt;

import jakarta.annotation.Resource;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/30 9:43
 */
@SpringBootTest
public class JasyptTest {

    @Resource
    private StringEncryptor stringEncryptor;

    @Test
    public void dencrypt() {
        String de = stringEncryptor.decrypt("coeWBAUYQfeWRGl96bPYUdE+BjP4qDueEKJ99nz6JQI=");
        System.out.println(de);
    }

}
