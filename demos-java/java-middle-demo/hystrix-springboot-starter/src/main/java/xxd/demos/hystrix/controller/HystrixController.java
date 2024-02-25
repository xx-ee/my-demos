package xxd.demos.hystrix.controller;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xxd.demos.hystrix.annotation.DoHystrix;

/**
 * Created by xiedong
 * Date: 2024/2/25 12:29
 */
@RestController
@RequestMapping("/hystrix")
@Slf4j
public class HystrixController {
    @GetMapping("/api/queryUserInfo")
    @DoHystrix(cacheKey = "#userId", useCacheAfter = true)
    public Object queryUserInfo(@RequestParam String userId) throws InterruptedException {
        log.info("查询用户信息，userId：{}", userId);
        if (RandomUtil.randomBoolean()) {
            int a = 1 / 0;
        }
        return new UserInfo("虫虫:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");
    }
}
