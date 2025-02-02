package vip.yeee.memoo.demo.stools.kit.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.springframework.stereotype.Component;
import vip.yeee.memoo.common.redisson.kit.DelayQueueKit;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/7/10 15:22
 */
@Slf4j
@Component
public class DelayQueueService {

    @Resource
    private DelayQueueKit delayQueueKit;

    private final static String TEST_QUEUE = DelayQueueKit.QUEUE_PREFIX + "TEST";

    public RBlockingDeque<Integer> getTestQueue() {
        return delayQueueKit.getDelayQueue(TEST_QUEUE);
    }

    public void addTestQueue(Integer ele, long delayTime) {
        log.info("【队列-TEST】- 添加元素，ele = {}", ele);
        delayQueueKit.addDelayQueue(TEST_QUEUE, ele, true, Math.max(delayTime - System.currentTimeMillis(), 0), TimeUnit.MILLISECONDS);
    }

    public void consumeTestQueueMsg(Consumer<Integer> handler) {
        delayQueueKit.consumeQueueMsg(TEST_QUEUE, handler);
    }

}
