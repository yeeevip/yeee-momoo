package vip.yeee.memoo.demo.thirdsdk.weixin.ma.service;

import org.springframework.stereotype.Service;
import vip.yeee.memoo.base.web.utils.SpringContextUtils;
import vip.yeee.memoo.common.wxsdk.ma.properties.WxMaProperties;
import vip.yeee.memoo.common.wxsdk.ma.service.IWxMaConfigService;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/4/8 14:43
 */
@Service
public class WxMaConfigService implements IWxMaConfigService {

    @Override
    public WxMaProperties.MaConfig findMaConfigByAppId(String appId) {
        WxMaProperties configPo = (WxMaProperties)SpringContextUtils.getBean(WxMaProperties.class);
        for (WxMaProperties.MaConfig config : configPo.getConfigs()) {
            if (appId.equals(config.getAppId())) {
                return config;
            }
        }
        return null;
    }
}
