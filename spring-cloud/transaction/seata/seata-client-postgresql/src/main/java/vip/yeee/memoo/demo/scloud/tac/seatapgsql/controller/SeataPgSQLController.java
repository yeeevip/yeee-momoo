package vip.yeee.memoo.demo.scloud.tac.seatapgsql.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memoo.base.model.rest.CommonResult;
import vip.yeee.memoo.demo.scloud.tac.seatapgsql.biz.SeataPgSQLBiz;

import jakarta.annotation.Resource;

@RestController
public class SeataPgSQLController {

    @Resource
    private SeataPgSQLBiz seataPgSQLBiz;

    @GetMapping("/seata/exec/opr")
    public CommonResult<Void> seataExecOpr() {
        return CommonResult.success(seataPgSQLBiz.seataExecOpr());
    }

}
