package xxd.demo.hystrix.controller;

import cn.hutool.core.util.StrUtil;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xxd.demo.hystrix.outer.ServerService;

/**
 * Created by xiedong
 * Date: 2024/2/25 12:29
 */
@RestController
@RequestMapping("/hystrix")
@Slf4j
public class ClientController {
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private ServerService userInfoOuterService;

    @GetMapping("/api/queryUserInfo")
    public Object queryUserInfo(@RequestParam long userId) throws InterruptedException {
        long start = System.currentTimeMillis();
        log.info("查询用户信息1，userId：{}", userId);
        String nickname = userInfoOuterService.getNickname(userId);
        String nickname1 = userInfoOuterService.getNickname(userId);
        log.info("查询用户信息1，userId：{} cost:{}", userId, System.currentTimeMillis() - start);
        return nickname;
    }




    @GetMapping("/api/queryUserInfo/cmdCache")
    public Object queryUserInfoSelfCache(@RequestParam long userId) throws InterruptedException {
        long start = System.currentTimeMillis();
        log.info("查询用户信息cmdCache，userId：{}", userId);
        String nickname = userInfoOuterService.getNicknameCmdCache(userId);
        log.info("查询用户信息cmdCache，userId：{} cost:{}", userId, System.currentTimeMillis() - start);
        return nickname;
    }



    @GetMapping("/api/queryUserInfoCache")
    public Object queryUserInfoWithCache(@RequestParam long userId) throws InterruptedException {
        //初始化Hystrix请求上下文
        HystrixRequestContext.initializeContext();
        //访问并开启缓存
        String result1 = userInfoOuterService.getNickname(userId); // 真正的http
        String result2 = userInfoOuterService.getNickname(userId); // 使用缓存
        log.info("first request result is:{} ,and secend request result is: {}", result1, result2);
        //清除缓存
        userInfoOuterService.flushCacheByAnnotation1(userId); //清除缓存
        //再一次访问并开启缓存
        String result3 = userInfoOuterService.getNickname(userId); // 真正的http
        String result4 = userInfoOuterService.getNickname(userId);  // 使用缓存
        log.info("first request result is:{} ,and secend request result is: {}", result3, result4);

        return StrUtil.join("；", result1, result2, result3, result4);
    }


    //    /**
//     * http://10.2.8.102:8081/hystrix/api/prop?hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=20
//     * 手动配置
//     *
//     * @param map
//     * @return
//     */
//    @GetMapping("/api/prop")
//    public Object putTimeOut(@RequestParam Map<String, Object> map) {
////        applicationContext.publishEvent(new HystrixPropertiesChangeEvent(this, map));
//        return "ok";
//    }
//
    @GetMapping("/api/forceopen")
    public Object putForceOpen(@RequestParam String key, @RequestParam boolean forceOpen) {
//        hystrixCacheService.putForceOpen(key, forceOpen);
//        return "结果：" + hystrixCacheService.getForceOpen(key);
        System.setProperty("hystrix.command.default.circuitBreaker.forceOpen", forceOpen + "");
        return "ok";
    }
}
