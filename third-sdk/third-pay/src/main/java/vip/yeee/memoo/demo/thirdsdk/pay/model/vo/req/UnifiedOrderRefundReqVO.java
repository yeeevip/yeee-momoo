package vip.yeee.memoo.demo.thirdsdk.pay.model.vo.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/3/16 11:55
 */
@Data
public class UnifiedOrderRefundReqVO {

    private String lesseeId;

    /**
     * 退款订单号（支付系统生成订单号）
     */
    @NotBlank
    private String outPayOrderId;

    /**
     * 支付订单号（与t_refund_order对应）
     */
    @NotBlank
    private String refundOrderCode;

    private String payOrderCode;

    @NotNull
    private Long amount;

    @NotNull
    private Long refundAmount;

    private String refundReason;

    private String payway;

}
