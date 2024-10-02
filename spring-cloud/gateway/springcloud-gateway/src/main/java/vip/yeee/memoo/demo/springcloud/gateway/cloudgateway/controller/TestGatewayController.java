package vip.yeee.memoo.demo.springcloud.gateway.cloudgateway.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memoo.base.model.rest.CommonResult;
import vip.yeee.memoo.demo.springcloud.gateway.cloudgateway.component.TestBiz;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/2/10 9:31
 */
@RestController
public class TestGatewayController {

    @Resource
    private TestBiz testBiz;

    @GetMapping("/gw/test/config-change")
    public CommonResult<Object> testConfigPropertyChange() {
        return CommonResult.success(testBiz.testConfigPropertyChange());
    }

}
