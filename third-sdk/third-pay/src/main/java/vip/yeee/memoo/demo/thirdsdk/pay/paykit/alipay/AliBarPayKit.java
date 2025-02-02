package vip.yeee.memoo.demo.thirdsdk.pay.paykit.alipay;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memoo.base.model.exception.BizException;
import vip.yeee.memoo.demo.thirdsdk.pay.model.bo.*;
import vip.yeee.memoo.demo.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memoo.demo.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memoo.demo.thirdsdk.pay.utils.AmountUtil;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/12/22 11:31
 */
@Slf4j
@Component
public class AliBarPayKit extends BaseAliPayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.ALI_BAR;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            AliPayConfigBO aliPayConfig = PayContext.getContext().getAliPayConfig();
            AlipayTradePayRequest req = new AlipayTradePayRequest();
            if (StrUtil.isNotBlank(aliPayConfig.getAuthToken())) {
                req.putOtherTextParam("app_auth_token", aliPayConfig.getAuthToken());
            }
            AlipayTradePayModel model = new AlipayTradePayModel();
            model.setOutTradeNo(reqBO.getOrderCode());
            model.setScene("bar_code"); //条码支付 bar_code ; 声波支付 wave_code
            model.setAuthCode(reqBO.getAuthCode()); //支付授权码
            model.setSubject(reqBO.getOrderSubject()); //订单标题
            model.setBody(reqBO.getOrderDesc());
            model.setTotalAmount(AmountUtil.convertCent2Dollar(reqBO.getPayMoney().toString()));
            model.setTimeExpire(LocalDateTimeUtil.format(reqBO.getExpireTime(), DatePattern.NORM_DATETIME_PATTERN));
//            req.setNotifyUrl(String.format(payProperties.getNotifyUrl(), PayConstant.PAY_WAY_CODE.ALI_BAR.toLowerCase()));
            req.setBizModel(model);

            CommonUnifiedOrderRespBO respBO = new CommonUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);

            AlipayTradePayResponse alipayResp = PayContext.getContext().getAlipayClient().execute(req);
            retMsgBO.setChannelAttach(alipayResp.getBody());
            retMsgBO.setChannelOrderId(alipayResp.getTradeNo());
            retMsgBO.setChannelUserId(alipayResp.getBuyerUserId());
            // 当条码重复发起时，支付宝返回的code = 10003, subCode = null [等待用户支付], 此时需要特殊判断 = = 。
            if ("10000".equals(alipayResp.getCode()) && alipayResp.isSuccess()) {
                retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS);
                retMsgBO.setChannelOrderId(alipayResp.getTradeNo());
            } else if ("10003".equals(alipayResp.getCode())) { //10003 表示为 处理中, 例如等待用户输入密码
                retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
                retMsgBO.setChannelOrderId(alipayResp.getTradeNo());
            } else { // 其他状态, 表示下单失败
                retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_FAIL);
                retMsgBO.setChannelErrCode(StrUtil.emptyToDefault(alipayResp.getCode(), alipayResp.getSubCode()));
                retMsgBO.setChannelErrMsg(alipayResp.getMsg() + "##" + alipayResp.getSubMsg());
            }
            return respBO;
        } catch (Exception e) {
            log.info("【统一下单-支付宝BAR支付】- 下单失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }

}
