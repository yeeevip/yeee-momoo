package vip.yeee.memoo.common.scloud.gray.gateway.handle;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import vip.yeee.memoo.common.scloud.gray.common.constant.CloudGrayConstant;
import vip.yeee.memoo.common.scloud.gray.gateway.context.GrayRequestContextHolder;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/12/6 15:24
 */
@Component
public class GlobalGrayFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //① 解析请求头，查看是否存在灰度发布的请求头信息，如果存在则将其放置在ThreadLocal中
        HttpHeaders headers = exchange.getRequest().getHeaders();
        if (headers.containsKey(CloudGrayConstant.API_VERSION_HEADER)) {
            String apiVersion = headers.getFirst(CloudGrayConstant.API_VERSION_HEADER);
            //②设置灰度标记
            GrayRequestContextHolder.setApiVersion(apiVersion);
            //③ 将灰度标记放入请求头中
            ServerHttpRequest request = exchange.getRequest().mutate()
                    //将灰度标记传递过去
                    .header(CloudGrayConstant.API_VERSION_HEADER, GrayRequestContextHolder.getApiVersion())
                    .build();
            ServerWebExchange build = exchange.mutate().request(request).build();
            return chain.filter(build).doFinally((s) -> GrayRequestContextHolder.remove());
        } else {
            return chain.filter(exchange);
        }
    }
}
