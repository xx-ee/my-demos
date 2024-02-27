package xxd.demo.hystrix.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xxd.demo.hystrix.outer.UserInfoOuterService;
import xxd.demos.hystrix.annotation.DoHystrix;
import xxd.demos.hystrix.listener.HystrixPropertiesChangeEvent;

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
    private ApplicationContext applicationContext;
    @Resource
    private UserInfoOuterService userInfoOuterService;

    /**
     * http://10.2.8.102:8081/hystrix/api/queryUserInfo?userId=1
     *
     * @param userId
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/api/queryUserInfo")
    @DoHystrix(
            groupKey = "queryUserInfoGroup",
            commandKey = "queryUserInfo",
            threadPoolKey = "queryUserInfoThreadPool",
            cacheKey = "#userId", useCacheAfter = true)
    public Object queryUserInfo(@RequestParam long userId) throws InterruptedException {
        long start = System.currentTimeMillis();
        log.info("查询用户信息，userId：{}", userId);
        String nickname = userInfoOuterService.getNickname(userId);
        log.info("查询用户信息，userId：{} cost:{}", userId, System.currentTimeMillis() - start);
        return nickname;
    }

    /**
     * http://10.2.8.102:8081/hystrix/api/prop?hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=20
     * 手动配置
     *
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
