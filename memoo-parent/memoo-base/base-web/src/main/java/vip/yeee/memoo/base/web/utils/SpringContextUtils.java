package vip.yeee.memoo.base.web.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * description......
 * @author https://www.yeee.vip
 */
@Component
public class SpringContextUtils implements  ApplicationContextAware , EmbeddedValueResolverAware {

    private static ApplicationContext applicationContext;
    private static StringValueResolver stringValueResolver;
    
    //获取上下文
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
 
    //设置上下文
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtils.applicationContext == null) {
            SpringContextUtils.applicationContext = applicationContext;
        }
    }

    public static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static HttpServletResponse getHttpServletResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getResponse();
    }
 
    //通过名字获取上下文中的bean
    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }
     
    //通过类型获取上下文中的bean
    public static Object getBean(Class<?> requiredType){
        return applicationContext.getBean(requiredType);
    }

    /**
     * SpringContextUtils.getPropertiesValue("onlineInterview.wxapp.appId")
     */
    public static String getPropertiesValue(String name) {
        try {
            return stringValueResolver.resolveStringValue("${" + name + "}");
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setEmbeddedValueResolver(@NonNull StringValueResolver stringValueResolver) {
        SpringContextUtils.stringValueResolver = stringValueResolver;
    }

}
