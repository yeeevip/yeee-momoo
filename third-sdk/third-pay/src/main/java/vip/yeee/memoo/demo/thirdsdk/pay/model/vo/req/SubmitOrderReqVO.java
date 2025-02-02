package vip.yeee.memoo.demo.thirdsdk.pay.model.vo.req;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/26 14:45
 */
@Data
public class SubmitOrderReqVO {

    @NotNull(message = "商品ID不能为空")
    private Long subjectId;

    // 订单金额，单位-分
    @NotNull(message = "订单金额不能为空")
    private BigDecimal amount;

}
