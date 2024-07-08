package xxd.demos.hystrx.outer.controller;

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
@RequestMapping("/outer/user/api")
@Slf4j
public class TestServerController {

    @GetMapping("/nickname/query")
    public String getNickname(@RequestParam long userId) throws InterruptedException {
        log.info("getNickname-start,userId:{}", userId);
        // http调用
        Thread.sleep(200);
        return "测试用户名：" + userId;
    }
}
