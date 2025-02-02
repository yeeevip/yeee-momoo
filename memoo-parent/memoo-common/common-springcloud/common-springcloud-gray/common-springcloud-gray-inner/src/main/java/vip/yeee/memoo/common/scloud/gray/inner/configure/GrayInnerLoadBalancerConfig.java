package vip.yeee.memoo.common.scloud.gray.inner.configure;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import vip.yeee.memoo.common.scloud.gray.common.handle.GrayLoadBalancer;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/12/6 17:29
 */
@Configuration
public class GrayInnerLoadBalancerConfig {

    @Bean
    public ReactorServiceInstanceLoadBalancer grayLoadBalancer(Environment environment, ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new GrayLoadBalancer(serviceInstanceListSupplierProvider, name);
    }

}
