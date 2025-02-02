package vip.yeee.memoo.demo.thirdsdk.pay.paykit.wxpay.v2;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memoo.base.model.exception.BizException;
import vip.yeee.memoo.base.util.JacksonUtils;
import vip.yeee.memoo.demo.thirdsdk.pay.model.bo.ChannelRetMsgBO;
import vip.yeee.memoo.demo.thirdsdk.pay.model.bo.UnifiedOrderReqBO;
import vip.yeee.memoo.demo.thirdsdk.pay.model.bo.UnifiedOrderRespBO;
import vip.yeee.memoo.demo.thirdsdk.pay.model.bo.WxJsapiUnifiedOrderRespBO;
import vip.yeee.memoo.demo.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memoo.demo.thirdsdk.pay.paykit.PayContext;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/12/20 19:51
 */
@Slf4j
@Component
public class WxJsapiPayKit extends BaseWxPayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_JSAPI;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            PayContext payContext = PayContext.getContext();
            WxPayUnifiedOrderRequest request = super.buildUnifiedOrderRequest(reqBO);
            request.setTradeType(WxPayConstants.TradeType.JSAPI);
            request.setNotifyUrl(getPayNotifyUrl());
            WxPayMpOrderResult response = payContext.getWxPayService().createOrder(request);
            WxJsapiUnifiedOrderRespBO respBO = new WxJsapiUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
            respBO.setMchId(payContext.getWxPayConfig().getSubMchId());
            respBO.setPayInfo(JacksonUtils.toJsonString(response));
            retMsgBO.setChannelAttach(respBO.getPayInfo());
            // 支付中
            retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
            retMsgBO.setChannelOrderId(response.getPackageValue().replaceFirst("prepay_id=", ""));
            return respBO;
        } catch (Exception e) {
            log.info("【统一下单-微信JSAPI支付】- 下单失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }

}
