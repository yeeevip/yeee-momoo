package vip.yeee.memoo.demo.thirdsdk.pay.model.bo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/26 16:50
 */
@Data
public class RefundOrderReqBO {

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

    private Long amount;

    private Long refundAmount;

    private String refundReason;

    private String refundNotifyUrl;

}
