package vip.yeee.memoo.demo.springcloud.apiauth.server.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/28 16:34
 */
@Data
public class UserLoginRequest {

    @NotBlank(message = "用户名不能空")
    private String username;
    @NotBlank(message = "密码不能空")
    private String password;

}
