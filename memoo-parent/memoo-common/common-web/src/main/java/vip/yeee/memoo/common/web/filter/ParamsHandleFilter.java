package vip.yeee.memoo.common.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

//@WebFilter(urlPatterns = {"/**"})
public class ParamsHandleFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) request) {
            @Override
            public String getParameter(String name) {
//                try {
                String parameter = super.getParameter(name);
                if (parameter == null || parameter.equals("")) {
                    return parameter;
                }
                String newStr = "";
                newStr = new String(parameter.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                return newStr;
//                if (Arrays.asList("get","GET").contains(super.getMethod())){
//                    return parameter;
//                }else{
//                    String newStr = "";
//                    try {
//                        newStr = new String(parameter.getBytes("ISO-8859-1"), "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    return newStr;
//                }
            }
        };
        chain.doFilter(requestWrapper,response);
    }

    @Override
    public void destroy() {

    }
}
