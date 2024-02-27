package xxd.demo.hystrix.controller;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xxd.demos.hystrix.annotation.DoHystrix;
import xxd.demos.hystrix.listener.HystrixPropertiesChangeEvent;
import xxd.demos.hystrix.service.HystrixCacheService;

import java.util.HashMap;
import java.util.Map;

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


    @Resource
    private ApplicationContext applicationContext;

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

    /**
     * http://10.2.8.102:8081/hystrix/api/prop?hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=20
     * 手动配置
     * @param map
     * @return
     */
    @GetMapping("/api/prop")
    public Object putTimeOut(@RequestParam Map<String, Object> map) {
        applicationContext.publishEvent(new HystrixPropertiesChangeEvent(this, map));
        return "ok";
    }

    @GetMapping("/api/forceopen")
    public Object putForceOpen(@RequestParam String key, @RequestParam boolean forceOpen) {
//        hystrixCacheService.putForceOpen(key, forceOpen);
//        return "结果：" + hystrixCacheService.getForceOpen(key);
        System.setProperty("hystrix.command.default.circuitBreaker.forceOpen", forceOpen + "");
        return "ok";
    }
}
