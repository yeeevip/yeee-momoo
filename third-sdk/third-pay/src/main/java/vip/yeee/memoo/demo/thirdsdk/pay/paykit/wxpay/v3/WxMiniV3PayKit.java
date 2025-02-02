package vip.yeee.memoo.demo.thirdsdk.pay.paykit.wxpay.v3;

import com.alibaba.fastjson.JSON;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memoo.base.model.exception.BizException;
import vip.yeee.memoo.demo.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memoo.demo.thirdsdk.pay.model.bo.*;
import vip.yeee.memoo.demo.thirdsdk.pay.paykit.PayContext;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/12/22 18:04
 */
@Slf4j
@Component
public class WxMiniV3PayKit extends WxV3PayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_MINI;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            PayContext payContext = PayContext.getContext();
            WxPayService wxPayService = payContext.getWxPayService();
            WxPayConfigBO wxPayConfig = payContext.getWxPayConfig();
            WxPayUnifiedOrderV3Result response;
            WxPayUnifiedOrderV3Request request = super.buildUnifiedOrderRequest(reqBO);
            request.setAppid(wxPayConfig.getMiniAppId());
            WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
            payer.setOpenid(reqBO.getOpenid());
            request.setPayer(payer);
            response = wxPayService.unifiedOrderV3(TradeTypeEnum.JSAPI, request);
            WxJsapiUnifiedOrderRespBO respBO = new WxJsapiUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
            respBO.setMchId(wxPayConfig.getMchId());
            Object payInfo = response.getPayInfo(TradeTypeEnum.JSAPI, wxPayConfig.getAppId(), wxPayConfig.getMchId(), wxPayService.getConfig().getPrivateKey());
            respBO.setPayInfo(JSON.toJSONString(payInfo));
            retMsgBO.setChannelAttach(respBO.getPayInfo());
            // 支付中
            retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
            retMsgBO.setChannelOrderId(response.getPrepayId());
            return respBO;
        } catch (Exception e) {
            log.info("【统一下单-微信JSAPI支付V3】- 下单失败", e);
            throw new BizException(e.getMessage());
        }
    }
}
