package xxd.demo.hystrix.outer;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import io.github.xxee.hystrix.cache.annotation.HystrixCmd;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by xiedong
 * 2024/2/27
 */
@Service
@Slf4j
public class ServerService {
    @Resource
    private RestTemplate restTemplate;


    @HystrixCmd(
            groupKey = "getNicknameWithCacheFirstGroup",
            commandKey = "getNicknameWithCacheFirst",
            threadPoolKey = "getNicknameWithCacheFirstThreadPool",
            cacheKey = "#userId",
            useCacheFirst = true)
    public String getNicknameWithCacheFirst(long userId) {
        log.info("getNicknameWithCacheFirst-call-outer,start,userId:{}", userId);
        String result = restTemplate.getForObject("http://localhost:8090/outer/user/api/nickname/query?userId=" + userId, String.class);
        log.info("getNicknameWithCacheFirst-call-outer,end,response:{}", result);
        return result;
    }

    @HystrixCmd(
            groupKey = "getNicknameFallbackCacheGroup",
            commandKey = "getNicknameFallbackCacheCache",
            threadPoolKey = "getNicknameFallbackCacheThreadPool",
            cacheKey = "#userId",
            useCacheAfter = true,
            fallbackDefaultJson = "")
    public String getNicknameFallbackCache(long userId) {
        log.info("getNicknameFallbackCache-call-outer,start,userId:{}", userId);
        String result = restTemplate.getForObject("http://localhost:8090/outer/user/api/nickname/query?userId=" + userId, String.class);
        log.info("getNicknameFallbackCache-call-outer,end,response:{}", result);
        return result;
    }

    @HystrixCmd(
            groupKey = "getNicknameNoCacheGroup",
            commandKey = "getNicknameNoCache",
            threadPoolKey = "getNicknameNoCacheThreadPool",
//            cacheKey = "#userId", useCacheFirst = true)
            cacheKey = "#userId", useCacheFirst = true)
    public String getNicknameCmdCache(long userId) {
        log.info("getNicknameCmdCache-call-outer,start,userId:{}", userId);
        String result = restTemplate.getForObject("http://localhost:8090/outer/user/api/nickname/query?userId=" + userId, String.class);
//        String result = restTemplate.getForObject("http://localhost:" + serverPort + "/outer/user/api/nickname/query?userId=" + userId, String.class);
        log.info("getNicknameCmdCache-call-outer,end,response:{}", result);
        return result;
    }


    //        @HystrixCmd(
//            groupKey = "getNicknameGroup",
//            commandKey = "getNickname",
//            threadPoolKey = "getNicknameThreadPool",
////            cacheKey = "#userId", useCacheFirst = true)
//            cacheKey = "#userId", useCacheAfter = true)
    @HystrixCommand(
            groupKey = "getNicknameGroup",
            commandKey = "getNickname",
            threadPoolKey = "getNicknameThreadPool",
            fallbackMethod = "getNicknameFallback"
    )
    @CacheResult(cacheKeyMethod = "getCacheKey")
    public String getNickname(long userId) {
        log.info("getNickname-call-outer,start,userId:{}", userId);
        String result = restTemplate.getForObject("http://localhost:8090/outer/user/api/nickname/query?userId=" + userId, String.class);
        log.info("getNickname-call-outer,end,response:{}", result);
        return result;
    }

    @CacheRemove(commandKey = "getNickname", cacheKeyMethod = "getCacheKey")
    @HystrixCommand
    public void flushCacheByAnnotation1(long userId) {
        log.info("请求缓存已清空！");
    }

    public String getCacheKey(long userId) {
        return String.valueOf(userId);
    }

    public String getNicknameFallback(long userId, Throwable throwable) {
        log.error("getNicknameFallback-error", throwable);
        return "fallback";
    }

}
