package vip.yeee.memoo.demo.scloud.rpc.feign03.biz;

import org.springframework.stereotype.Component;
import vip.yeee.memoo.demo.scloud.rpc.feign03.feign.Feign01FeignClient;
import vip.yeee.memoo.demo.scloud.rpc.feign03.feign.Feign02FeignClient;

import jakarta.annotation.Resource;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/2/21 14:06
 */
@Component
public class Feign03Biz {

    @Resource
    private Feign01FeignClient feign01FeignClient;
    @Resource
    private Feign02FeignClient feign02FeignClient;

    public String getData() {
        return feign01FeignClient.getData().getData() + feign02FeignClient.getData().getData();
    }
}
