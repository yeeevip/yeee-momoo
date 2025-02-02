package vip.yeee.memoo.demo.thirdsdk.pay.model.vo.resp;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/26 14:45
 */
@Data
@AllArgsConstructor
public class SubmitOrderRespVO {

    private String orderCode;

    private String amount;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime expiredTime;

}
