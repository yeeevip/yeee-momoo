package vip.yeee.memoo.demo.springboot.extpoint;

import lombok.extern.slf4j.Slf4j;

/**
 * 扩展点08：@PostConstruct
 *
 * @PostContruct全限定类名是javax.annotation.PostConstruct，可以看出来其本身不是Spring定义的注解，但是Spring提供了具体的实现
 *
 * 1. 时机：bean实例化(Instantiation)后，初始化(Initialization)之前
 * 在Spring容器启动的过程，被@PostConstruct标记的方法是怎么被执行的？
 * 内置BeanPostProcessor的实现InitDestroyAnnotationBeanPostProcessor.postProcessBeforeInitialization
 *
 * 1.1 先查询被@PostConstruct标记的方法
 *
 * 1.1 使用java反射去执行这个方法
 *
 *
 * @author https://www.yeee.vip
 * @since 2023/8/14 10:01
 */
@Slf4j
public class Ext08AnnoPostConstruct {

}
