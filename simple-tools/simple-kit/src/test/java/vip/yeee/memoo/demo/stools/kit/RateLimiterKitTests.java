package vip.yeee.memoo.demo.stools.kit;

import cn.hutool.core.thread.ThreadUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RRateLimiter;
import org.springframework.boot.test.context.SpringBootTest;
import vip.yeee.memoo.common.redisson.kit.RateLimiterKit;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/12/19 18:00
 */
@Slf4j
@SpringBootTest
public class RateLimiterKitTests {

    @Resource
    private RateLimiterKit rateLimiterKit;
    private RRateLimiter rRateLimiter = null;

    @PostConstruct
    public void init() {
        rRateLimiter = rateLimiterKit.rateLimiter("yeee:test:rate-limiter");
    }

    @Test
    public void testRateLimiter() throws InterruptedException {
        ThreadUtil.concurrencyTest(20, () -> {
            rRateLimiter.acquire();
            log.info("-----------当前线程 - {}", Thread.currentThread().getName());
        });
    }

}
