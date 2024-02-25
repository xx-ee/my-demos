package xxd.demos.hystrix.service.impl;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xxd.demos.hystrix.service.HystrixCacheService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;

/**
 * Created by xiedong
 * Date: 2024/2/25 16:40
 */
@Service
@Slf4j
public class HystrixCacheServiceImpl implements HystrixCacheService {
    @Resource
    private CacheManager cacheManager;
    private Cache<String, String> userCache;

    @PostConstruct
    public void init() {
        QuickConfig qc = QuickConfig.newBuilder("userCache")
                .expire(Duration.ofSeconds(5))
                .cacheType(CacheType.LOCAL) // two level cache
                .localLimit(50)
                .syncLocal(true) // invalidate local cache in all jvm process after update
                .build();
        userCache = cacheManager.getOrCreateCache(qc);
    }

    @Override
    public String getFallbackData(String key) {
        return userCache.get(key);
    }

    @Override
    public void putFallBackData(String key, Object val) {
        userCache.put(key, JSON.toJSONString(val));
    }
}
