package xxd.demos.hystrix.service.impl;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xxd.demos.hystrix.service.HystrixCacheService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xiedong
 * Date: 2024/2/25 16:40
 */
@Service
@Slf4j
public class HystrixCacheServiceImpl implements HystrixCacheService {
    @Resource
    private CacheManager cacheManager;
    private Cache<String, Object> userCache;

    private ConcurrentHashMap<String, Integer> exeTimeoutMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Boolean> exeForceOpenMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        QuickConfig qc = QuickConfig.newBuilder("userCache")
                .expire(Duration.ofSeconds(5))
                .cacheType(CacheType.LOCAL) // two level cache
                .localLimit(50)
                .syncLocal(true) // invalidate local cache in all jvm process after update
                .build();
        userCache = cacheManager.getOrCreateCache(qc);

        exeTimeoutMap.put("queryUserInfo", 200);
        exeForceOpenMap.put("queryUserInfo", false);
    }

    @Override
    public Object getFallbackData(String key) {
        return userCache.get(key);
    }

    @Override
    public void putFallBackData(String key, Object val) {
        userCache.put(key, JSON.toJSONString(val));
    }


    @Override
    public int getExeTimeout(String commandKey) {
        return exeTimeoutMap.get(commandKey);
    }

    @Override
    public void putExeTimeout(String commandKey, int exeTimeout) {
        // https://github.com/Netflix/Hystrix/wiki/Configuration#execution.isolation.thread.timeoutInMilliseconds
        exeTimeoutMap.put(commandKey, exeTimeout);
        System.setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", exeTimeout + "");
        // 获取 Hystrix 类的 commandProperties 字段
        removeKey(commandKey);

    }

    @Override
    public void putForceOpen(String commandKey, boolean forceOpen) {
        exeForceOpenMap.put(commandKey, forceOpen);
        System.setProperty("hystrix.command.default.circuitBreaker.forceOpen", forceOpen + "");
        removeKey(commandKey);
    }

    private static void removeKey(String commandKey) {
//        // 获取 Hystrix 类的 commandProperties 字段
//        Map<String, HystrixCommandProperties> commandPropertiesMap = (Map<String, HystrixCommandProperties>)
//                ReflectUtil.getFieldValue(HystrixPropertiesFactory.class, "commandProperties");
//        ConcurrentHashMap<String, HystrixThreadPoolProperties> threadPoolProperties = (ConcurrentHashMap<String, HystrixThreadPoolProperties>)
//                ReflectUtil.getFieldValue(HystrixPropertiesFactory.class, "threadPoolProperties");
//
//        ConcurrentHashMap<String, HystrixCollapserProperties> collapserProperties = (ConcurrentHashMap<String, HystrixCollapserProperties>)
//                ReflectUtil.getFieldValue(HystrixPropertiesFactory.class, "collapserProperties");
//
//        commandPropertiesMap.remove(commandKey);
//        threadPoolProperties.remove(commandKey);
//        collapserProperties.remove(commandKey);
    }

    @Override
    public boolean getForceOpen(String commandKey) {
        return exeForceOpenMap.get(commandKey);
    }
}
