package xxd.demos.hystrx.outer.controller;

import cn.hutool.core.util.RandomUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiedong
 * 2024/2/27
 */
@RestController
@RequestMapping("/outer/interact/api")
@Slf4j
public class InteractController {

    @GetMapping("/like/count/query")
    public String getLikeCount(String contentId) throws InterruptedException {
        log.info("getLikeCount-start,contentId:{}", contentId);
        // http调用
        Thread.sleep(RandomUtil.randomInt(50));
        return RandomUtil.randomInt(10) + "万";
    }

    @GetMapping("/like/islike/query")
    public boolean checkLiked(long userId, String contentId) throws InterruptedException {
        log.info("checkLiked-start,userId:{},contentId:{}", userId, contentId);
        // http调用
        Thread.sleep(RandomUtil.randomInt(50));
        return RandomUtil.randomBoolean();
    }
}
