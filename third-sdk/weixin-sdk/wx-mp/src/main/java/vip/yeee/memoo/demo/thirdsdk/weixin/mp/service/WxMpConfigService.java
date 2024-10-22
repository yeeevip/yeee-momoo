package vip.yeee.memoo.demo.thirdsdk.weixin.mp.service;

import org.springframework.stereotype.Service;
import vip.yeee.memoo.base.web.utils.SpringContextUtils;
import vip.yeee.memoo.common.wxsdk.mp.properties.WxMpProperties;
import vip.yeee.memoo.common.wxsdk.mp.service.IWxMpConfigService;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/4/8 14:43
 */
@Service
public class WxMpConfigService implements IWxMpConfigService {
    @Override
    public WxMpProperties.MpConfig findMpConfigByAppId(String appId) {
        WxMpProperties configPo = (WxMpProperties)SpringContextUtils.getBean(WxMpProperties.class);
        for (WxMpProperties.MpConfig config : configPo.getConfigs()) {
            if (appId.equals(config.getAppId())) {
                return config;
            }
        }
        return null;
    }
}
