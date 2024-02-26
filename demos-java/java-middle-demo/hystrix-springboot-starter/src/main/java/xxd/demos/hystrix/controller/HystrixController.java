package xxd.demos.hystrix.controller;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xxd.demos.hystrix.annotation.DoHystrix;
import xxd.demos.hystrix.service.HystrixCacheService;

/**
 * Created by xiedong
 * Date: 2024/2/25 12:29
 */
@RestController
@RequestMapping("/hystrix")
@Slf4j
public class HystrixController {
    @Resource
    private HystrixCacheService hystrixCacheService;

    @GetMapping("/api/queryUserInfo")
    @DoHystrix(
            groupKey = "queryUserInfoGroup",
            commandKey = "queryUserInfo",
            threadPoolKey = "queryUserInfoThreadPool",
            cacheKey = "#userId", useCacheAfter = true)
    public Object queryUserInfo(@RequestParam String userId) throws InterruptedException {
        long start = System.currentTimeMillis();
        log.info("查询用户信息，userId：{}", userId);
//        if (RandomUtil.randomBoolean()) {
//            int a = 1 / 0;
//        }
        Thread.sleep(100);
        UserInfo userInfo = new UserInfo("虫虫:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");
        log.info("查询用户信息，userId：{} cost:{}", userId, System.currentTimeMillis() - start);
        return userInfo;
    }

    @GetMapping("/api/timeout")
    public Object putTimeOut(@RequestParam String key, @RequestParam Integer timeout) {
        hystrixCacheService.putExeTimeout(key, timeout);
        return "结果：" + hystrixCacheService.getExeTimeout(key);
    }

    @GetMapping("/api/forceopen")
    public Object putForceOpen(@RequestParam String key, @RequestParam boolean forceOpen) {
        hystrixCacheService.putForceOpen(key, forceOpen);
        return "结果：" + hystrixCacheService.getForceOpen(key);
    }
}
