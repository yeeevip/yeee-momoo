package vip.yeee.memoo.demo.scloud.rpc.feign03.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memoo.base.model.rest.CommonResult;
import vip.yeee.memoo.demo.scloud.rpc.feign03.biz.Feign03Biz;

import jakarta.annotation.Resource;

@RestController
public class Feign03Controller {

    @Resource
    private Feign03Biz feign03Biz;

    @GetMapping("call/get-data")
    public CommonResult<String> getData() {
        return CommonResult.success(feign03Biz.getData());
    }

}
