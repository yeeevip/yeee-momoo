package vip.yeee.memo.demo.thirdsdk.pay.model.vo.req;

import lombok.Data;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/12/21 15:20
 */
@Data
public class UnifiedOrderQueryReqVO {

    private String lesseeId;
    private String orderCode;
    private String payway;
}
