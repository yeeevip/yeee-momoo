package vip.yeee.memoo.common.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 自定义过滤器
 *
 * @author https://www.yeee.vip
 * @since 2022/7/12 14:25
 */
@Slf4j
public class FilterDemo implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("【自定义过滤器】 - 执行前 - uri = {}", ((HttpServletRequest)servletRequest).getRequestURI());
        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
        log.info("【自定义过滤器】 - 执行后 - uri = {}", ((HttpServletRequest)servletRequest).getRequestURI());
    }

}
