package vip.yeee.memoo.common.wxsdk.ma.service;

import vip.yeee.memoo.common.wxsdk.ma.properties.WxMaProperties;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/4/8 14:29
 */
public interface IWxMaConfigService {

    WxMaProperties.MaConfig findMaConfigByAppId(String appId);

}
