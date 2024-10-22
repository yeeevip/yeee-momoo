package vip.yeee.memoo.demo.springboot;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import vip.yeee.memoo.demo.springboot.transaction.TransactionExample;

import jakarta.annotation.Resource;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/7/12 9:14
 */
@Slf4j
@SpringBootTest
public class TransactionTests {

    @Resource
    private TransactionExample transactionExample;

    @Test
    public void test() throws Exception {
        transactionExample.doSomething();
        transactionExample.doSomething();
    }

}
