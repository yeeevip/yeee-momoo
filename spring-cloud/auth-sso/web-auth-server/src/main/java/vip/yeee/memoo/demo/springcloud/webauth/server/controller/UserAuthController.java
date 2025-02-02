package vip.yeee.memoo.demo.springcloud.webauth.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memoo.demo.springcloud.webauth.server.biz.UserAuthBiz;
import vip.yeee.memoo.demo.springcloud.webauth.server.model.request.UserAuthRequest;
import vip.yeee.memoo.base.model.rest.CommonResult;
import vip.yeee.memoo.demo.springcloud.webauth.server.model.vo.UserAuthVo;

import jakarta.annotation.Resource;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/16 17:33
 */
@RestController
public class UserAuthController {

    @Resource
    private UserAuthBiz userAuthBiz;

    @PostMapping("/system/register")
    public CommonResult<Void> systemUserRegister(@RequestBody UserAuthRequest request) {
        return CommonResult.success(userAuthBiz.systemUserRegister(request));
    }

    @PostMapping("/system/login")
    public CommonResult<UserAuthVo> systemUserLogin(@RequestBody UserAuthRequest request) {
        return CommonResult.success(userAuthBiz.systemUserLogin(request));
    }

    @GetMapping("/system/logout")
    public CommonResult<Void> userLogout() {
        return CommonResult.success(userAuthBiz.userLogout());
    }

}
