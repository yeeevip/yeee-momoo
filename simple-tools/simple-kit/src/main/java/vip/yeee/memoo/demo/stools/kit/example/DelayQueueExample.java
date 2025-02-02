package vip.yeee.memoo.demo.stools.kit.example;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import vip.yeee.memoo.demo.stools.kit.service.DelayQueueService;

import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/9/17 17:40
 */
@Slf4j
@Component
public class DelayQueueExample implements ApplicationRunner {

    @Resource
    private DelayQueueService delayQueueService;

/*    @PostConstruct
    public void init() throws Exception {
//        ThreadUtil.execAsync(() -> {
//            try {
//                this.gen();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        ThreadUtil.execAsync(this::get);
        new Thread(this::get).start();
    }*/

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        ThreadUtil.execAsync(() -> {
//            try {
//                this.gen();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        ThreadUtil.execAsync(this::get);
        new Thread(() -> delayQueueService.consumeTestQueueMsg(this::handle)).start();
    }

    private void gen() throws InterruptedException {
        int id = 10000;
        while (true) {
            delayQueueService.addTestQueue(id, System.currentTimeMillis() + 100);
            TimeUnit.SECONDS.sleep(10);
//            id = id + 1;
        }
    }

    // new Thread(this::get).start();
    private void handle(Integer ele) {

    }

}
