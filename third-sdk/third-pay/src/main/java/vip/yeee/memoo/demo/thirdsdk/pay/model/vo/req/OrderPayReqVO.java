package vip.yeee.memoo.demo.thirdsdk.pay.model.vo.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import vip.yeee.memoo.demo.thirdsdk.pay.constant.PayConstant;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/26 15:56
 */
@Data
public class OrderPayReqVO {

    /**
     * @see  PayConstant.PAY_WAY_CODE
     */
    @NotBlank(message = "支付方式不能为空")
    private String payway;

    @NotNull(message = "订单金额不能为空")
    private BigDecimal amount;

    @NotNull(message = "订单编号不能为空")
    private String orderCode;

    private String openId;

    private String appId;

    private String payInfo;

}
