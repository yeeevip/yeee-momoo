package vip.yeee.memo.integrate.thirdsdk.pay.model.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 11:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WxAppUnifiedOrderRespBO extends UnifiedOrderRespBO {

    /** 预支付数据包 **/
    private String payInfo;

}
