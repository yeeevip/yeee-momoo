package vip.yeee.memoo.demo.springcloud.webauth.server.model.request;

import lombok.Data;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/17 13:22
 */
@Data
public class UserAuthRequest {

    private String username;
    private String password;

}
