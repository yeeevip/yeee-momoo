package vip.yeee.memoo.demo.scloud.rpc.feign01.controller;

import org.springframework.web.bind.annotation.*;
import vip.yeee.memoo.base.model.rest.CommonResult;

@RestController
public class Feign01Controller {

    @GetMapping("call/get-data")
    public CommonResult<String> getData() {
        return CommonResult.success("Hello");
    }

}
