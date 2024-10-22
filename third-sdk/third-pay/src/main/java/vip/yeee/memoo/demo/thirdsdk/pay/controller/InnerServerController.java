package vip.yeee.memoo.demo.thirdsdk.pay.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import vip.yeee.memoo.base.model.rest.CommonResult;
import vip.yeee.memoo.demo.thirdsdk.pay.model.bo.ChannelRetMsgBO;
import vip.yeee.memoo.demo.thirdsdk.pay.model.bo.UnifiedOrderRespBO;
import vip.yeee.memoo.demo.thirdsdk.pay.model.vo.req.UnifiedOrderQueryReqVO;
import vip.yeee.memoo.demo.thirdsdk.pay.model.vo.req.UnifiedOrderRefundReqVO;
import vip.yeee.memoo.demo.thirdsdk.pay.model.vo.req.UnifiedOrderReqVO;
import vip.yeee.memoo.demo.thirdsdk.pay.service.UnifiedPayOrderService;

import jakarta.annotation.Resource;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/26 14:41
 */
@RestController
public class InnerServerController {

    @Resource
    private UnifiedPayOrderService unifiedPayOrderService;

    @PostMapping("/inner/order/pay")
    public CommonResult<UnifiedOrderRespBO> unifiedOrder(@Valid @RequestBody UnifiedOrderReqVO reqVO) throws Exception {
        return CommonResult.success(unifiedPayOrderService.unifiedOrder(reqVO));
    }

    @PostMapping("/inner/order/refund")
    public CommonResult<ChannelRetMsgBO> unifiedOrderRefund(@Valid @RequestBody UnifiedOrderRefundReqVO reqVO) {
        return CommonResult.success(unifiedPayOrderService.unifiedOrderRefund(reqVO));
    }

    @PostMapping("/inner/order/query")
    public CommonResult<ChannelRetMsgBO> queryOrder(@Valid @RequestBody UnifiedOrderQueryReqVO request) {
        return CommonResult.success(unifiedPayOrderService.queryOrder(request));
    }

}
