package vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/21 15:20
 */
@Data
public class OrderQueryReqVO {

    @NotBlank
    private String orderCode;
}