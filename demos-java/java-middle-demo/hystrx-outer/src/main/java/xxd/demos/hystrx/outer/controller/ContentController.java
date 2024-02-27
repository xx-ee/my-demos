package xxd.demos.hystrx.outer.controller;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiedong
 * 2024/2/27
 */
@Slf4j
@RestController
@RequestMapping("/outer/content/api")
public class ContentController {

    @GetMapping("/content/titile/query")
    public String getContentTitle(@RequestParam String contentId) throws InterruptedException {
        log.info("getContentTitle-start,contentId:{}", contentId);
        // http调用
        Thread.sleep(RandomUtil.randomInt(50));
        return "这是一个标题：" + contentId;
    }
}
