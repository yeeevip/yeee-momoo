package vip.yeee.memoo.demo.scloud.tac.seata03.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vip.yeee.memoo.base.model.rest.CommonResult;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/18 18:33
 */
@FeignClient(name = "seata-client-01", path = "/client01")
public interface SeataClient01FeignClient {

    @GetMapping("/seata/exec/opr")
    CommonResult<Void> seataExecOpr();

}
