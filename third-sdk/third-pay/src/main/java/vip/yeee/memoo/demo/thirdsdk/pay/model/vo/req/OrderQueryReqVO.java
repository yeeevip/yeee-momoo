package vip.yeee.memoo.demo.thirdsdk.pay.model.vo.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/12/21 15:20
 */
@Data
public class OrderQueryReqVO {

    @NotBlank
    private String orderCode;
}
