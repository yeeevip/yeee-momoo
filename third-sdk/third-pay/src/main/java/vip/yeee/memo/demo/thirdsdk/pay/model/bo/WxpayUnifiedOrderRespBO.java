package vip.yeee.memo.demo.thirdsdk.pay.model.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/26 11:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WxpayUnifiedOrderRespBO extends UnifiedOrderRespBO {

    private String tradeType;

    private String prepayId;

}
