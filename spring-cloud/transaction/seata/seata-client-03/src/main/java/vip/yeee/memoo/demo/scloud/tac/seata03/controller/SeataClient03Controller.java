package vip.yeee.memoo.demo.scloud.tac.seata03.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memoo.base.model.rest.CommonResult;
import vip.yeee.memoo.demo.scloud.tac.seata03.biz.SeataClient03Biz;

import jakarta.annotation.Resource;

@RestController
public class SeataClient03Controller {

    @Resource
    private SeataClient03Biz seataClient03Biz;

    @GetMapping("/seata/exec/opr")
    public CommonResult<Void> seataExecOpr() {
        return CommonResult.success(seataClient03Biz.seataExecOpr());
    }

    @GetMapping("/seata/exec/mixdatabase/opr")
    public CommonResult<Void> seataExecMixDatabaseOpr() {
        return CommonResult.success(seataClient03Biz.seataExecMixDatabaseOpr());
    }

}
