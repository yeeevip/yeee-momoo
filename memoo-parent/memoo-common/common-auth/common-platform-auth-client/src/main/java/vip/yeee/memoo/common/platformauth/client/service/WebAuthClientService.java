//package vip.yeee.memoo.common.platformauth.client.service;
//
//import cn.hutool.core.util.StrUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
//import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.stereotype.Component;
//import vip.yeee.memoo.base.model.exception.BizException;
//import vip.yeee.memoo.base.websecurityoauth2.constant.AuthConstant;
//import vip.yeee.memoo.base.websecurityoauth2.constant.MessageConstant;
//import vip.yeee.memoo.base.websecurityoauth2.model.Oauth2TokenVo;
//
//import jakarta.annotation.Resource;
//
///**
// * description......
// *
// * @author https://www.yeee.vip
// * @since 2023/3/8 10:58
// */
//@Slf4j
//@Component
//public class WebAuthClientService {
//    @Resource
//    private OAuth2RestTemplate oAuth2RestTemplate;
//
//    public Oauth2TokenVo getUserAccessToken(String userType, String username, String password) {
//        log.info("getUserAccessToken, userType = {}, username = {}, password = {}", userType, username, password);
//        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
//            throw new BizException(MessageConstant.USERNAME_PASSWORD_ERROR);
//        }
//        ResourceOwnerPasswordResourceDetails passwordResourceDetails =
//                (ResourceOwnerPasswordResourceDetails) this.oAuth2RestTemplate.getResource();
//        passwordResourceDetails.setUsername(userType + AuthConstant.USERNAME_SEPARATOR + username);
//        passwordResourceDetails.setPassword(password);
//        oAuth2RestTemplate.getOAuth2ClientContext().setAccessToken(null);
//        OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();
//        Oauth2TokenVo oauth2TokenVo = new Oauth2TokenVo();
//        oauth2TokenVo.setToken(accessToken.getValue());
//        oauth2TokenVo.setRefreshToken(accessToken.getRefreshToken().getValue());
//        oauth2TokenVo.setExpiresIn(accessToken.getExpiresIn());
//        oauth2TokenVo.setTokenHead(AuthConstant.JWT_TOKEN_PREFIX);
//        return oauth2TokenVo;
//    }
//
//}
