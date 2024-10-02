package vip.yeee.memoo.common.platformauth.client.handle;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import vip.yeee.memoo.base.model.rest.CommonResult;
import vip.yeee.memoo.common.platformauth.client.utils.HttpResponseUtils;

import java.io.IOException;

/**
 * 处理器
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        HttpResponseUtils.write(response, CommonResult.forbidden(""));
    }

}
