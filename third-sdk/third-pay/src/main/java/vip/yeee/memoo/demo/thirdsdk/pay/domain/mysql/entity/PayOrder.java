package vip.yeee.memoo.demo.thirdsdk.pay.domain.mysql.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_pay_order")
public class PayOrder {

    @TableId
    private Long id;

    private String lesseeId;

    /**
     * 订单号
     */
    private String code;

    private String outCode;

    private String mchId;

    /**
     * 支付状态: 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-已退款, 6-订单关闭
     */
    private Integer state;

    /**
     * 支付金额,单位分
     */
    private BigDecimal amount;

    /**
     * 三位货币代码,人民币:cny
     */
    private String currency;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 支付渠道
     */
    private String channel;

    /**
     * 支付方式
     */
    private String payway;

    /**
     * 渠道用户标识,如微信openId,支付宝账号
     */
    private String channelUser;

    /**
     * 退款状态: 0-未发生实际退款, 1-部分退款, 2-全额退款
     */
    private Integer refundState;

    /**
     * 退款总金额,单位分
     */
    private BigDecimal refundAmount;

    /**
     * 订单失效时间
     */
    private LocalDateTime expiredTime;

    /**
     * 订单支付成功时间
     */
    private LocalDateTime successTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Integer deleted;

}
