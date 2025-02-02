package vip.yeee.memoo.common.redisson.kit;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 判断【不存在则一定不存在】，判断存在则可能误判(hash冲突)
 *
 * @author https://www.yeee.vip
 * @since 2022/9/17 18:28
 */
@Slf4j
@Component
public class BloomFilterKit {

    @Resource
    private RedissonClient redissonClient;

    public void initOrAddEle(String name, List<String> elements) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(name);
        if (!bloomFilter.isExists()) {
            log.info("【布隆过滤器】- 初始化 - name = {}", name);
            bloomFilter.tryInit(elements.size(), 0.01);
//        bloomFilter.expire(expired, TimeUnit.MINUTES);
        }
        if (!CollectionUtils.isEmpty(elements)) {
            log.info("【布隆过滤器】- 添加元素 - elementsSize = {}", elements.size());
            elements.forEach(bloomFilter::add);
        }
    }

    public boolean checkContainsEle(String name, String ele) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(name);
        if (bloomFilter == null || !StringUtils.hasText(ele)) {
            return false;
        }
        return bloomFilter.contains(ele);
    }

}
