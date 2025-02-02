package vip.yeee.memoo.demo.springcloud.apiauth.server.biz;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memoo.common.appauth.client.context.ApiSecurityContext;
import vip.yeee.memoo.common.appauth.client.kit.JwsClientKit;
import vip.yeee.memoo.common.appauth.client.model.ApiAuthedUser;
import vip.yeee.memoo.common.appauth.client.model.dto.PayloadDto;
import vip.yeee.memoo.common.appauth.server.kit.JwsServerKit;
import vip.yeee.memoo.common.appauth.server.kit.JwtServerKit;
import vip.yeee.memoo.common.appauth.server.model.vo.JTokenVo;
import vip.yeee.memoo.base.model.exception.BizException;
import vip.yeee.memoo.base.util.JacksonUtils;
import vip.yeee.memoo.common.domain.entity.front.User;
import vip.yeee.memoo.common.domain.mapper.front.UserMapper;
import vip.yeee.memoo.demo.springcloud.apiauth.server.model.request.UserLoginRequest;

import jakarta.annotation.Resource;
import java.util.Map;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/28 16:47
 */
@Slf4j
@Component
public class ApiAuthBiz {

    @Resource
    private UserMapper userMapper;
    @Resource
    private JwtServerKit jwtServerKit;
    @Resource
    private JwsServerKit jwsServerKit;
    @Resource
    private JwsClientKit jwsClientKit;

    public JTokenVo userLogin(UserLoginRequest request) throws Exception {
        LambdaQueryWrapper<User> query = Wrappers.lambdaQuery();
        query.eq(User::getUsername, request.getUsername());
        User user = userMapper.selectOne(query);
        if (user == null || !SecureUtil.md5().digestHex(request.getPassword()).equals(user.getPassword())) {
            throw new BizException("用户名或者密码错误");
        }
        Map<String, Object> map = ImmutableMap.of("id", user.getId(), "username", user.getUsername());
        return jwtServerKit.createToken(JacksonUtils.toJsonString(map));
    }

    public String accessApi() {
        ApiAuthedUser curUser = ApiSecurityContext.getCurUser();
        if (curUser == null) {
            return "匿名用户访问接口成功";
        }
        return StrUtil.format("id = {}, username = {}, 访问接口成功", curUser.getId(), curUser.getUsername());
    }

    public String getJwsToken() {
        try {
            Map<String, Object> map = ImmutableMap.of("id", 111112, "username", "一页");
            return jwsServerKit.generateTokenByRSA(JacksonUtils.toJsonString(map));
        } catch (Exception e) {
            log.info("生成JWS token失败", e);
            throw new BizException("生成JWS token失败：" + e.getMessage());
        }
    }

    public Void verifyJwsToken(String token) {
        try {
            PayloadDto payloadDto = jwsClientKit.verifyTokenByRSA(token);
            log.info("SUBJECT = {}", payloadDto.getSub());
        } catch (Exception e) {
            log.info("验证JWS token失败", e);
            throw new BizException("验证JWS token失败：" + e.getMessage());
        }
        return null;
    }
}
