package xxd.demos.hystrix.service.impl;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import xxd.demos.hystrix.service.HystrixCacheService;

/**
 * Created by xiedong
 * Date: 2024/2/25 16:40
 */
public class HystrixCacheServiceImpl implements HystrixCacheService {
    private Cache<String, Object> hystrixCache;


    public HystrixCacheServiceImpl(Cache<String, Object> hystrixCache) {
        this.hystrixCache = hystrixCache;
    }

    @Override
    public Object getFallbackData(String key) {
        return hystrixCache.get(key);
    }

    @Override
    public void putFallBackData(String key, Object val) {
        hystrixCache.put(key, JSON.toJSONString(val));
    }


//    @Override
//    public int getExeTimeout(String commandKey) {
//        return exeTimeoutMap.get(commandKey);
//    }
//
//    @Override
//    public void putExeTimeout(String commandKey, int exeTimeout) {
//        // https://github.com/Netflix/Hystrix/wiki/Configuration#execution.isolation.thread.timeoutInMilliseconds
//        exeTimeoutMap.put(commandKey, exeTimeout);
//        System.setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", exeTimeout + "");
//        // 获取 Hystrix 类的 commandProperties 字段
//        removeKey(commandKey);
//
//    }
//
//    @Override
//    public void putForceOpen(String commandKey, boolean forceOpen) {
//        exeForceOpenMap.put(commandKey, forceOpen);
//        System.setProperty("hystrix.command.default.circuitBreaker.forceOpen", forceOpen + "");
//        removeKey(commandKey);
//    }
//
//    private static void removeKey(String commandKey) {
////        // 获取 Hystrix 类的 commandProperties 字段
////        Map<String, HystrixCommandProperties> commandPropertiesMap = (Map<String, HystrixCommandProperties>)
////                ReflectUtil.getFieldValue(HystrixPropertiesFactory.class, "commandProperties");
////        ConcurrentHashMap<String, HystrixThreadPoolProperties> threadPoolProperties = (ConcurrentHashMap<String, HystrixThreadPoolProperties>)
////                ReflectUtil.getFieldValue(HystrixPropertiesFactory.class, "threadPoolProperties");
////
////        ConcurrentHashMap<String, HystrixCollapserProperties> collapserProperties = (ConcurrentHashMap<String, HystrixCollapserProperties>)
////                ReflectUtil.getFieldValue(HystrixPropertiesFactory.class, "collapserProperties");
////
////        commandPropertiesMap.remove(commandKey);
////        threadPoolProperties.remove(commandKey);
////        collapserProperties.remove(commandKey);
//    }
//
//    @Override
//    public boolean getForceOpen(String commandKey) {
//        return exeForceOpenMap.get(commandKey);
//    }
}
