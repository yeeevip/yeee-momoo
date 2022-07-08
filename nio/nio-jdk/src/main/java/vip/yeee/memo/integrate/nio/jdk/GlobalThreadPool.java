package vip.yeee.memo.integrate.nio.jdk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/11/24 11:27
 */
public interface GlobalThreadPool {
    ExecutorService REACTOR_EXECUTOR = new ThreadPoolExecutor(3, 3, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(10000));
    ExecutorService WORK_EXECUTOR = new ThreadPoolExecutor(3, 3, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(10000));
    ExecutorService PIPE_EXECUTOR = new ThreadPoolExecutor(3, 3, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(10000));
}