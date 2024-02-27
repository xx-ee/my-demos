package xxd.demo.hystrix.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xxd.demo.hystrix.service.RecommendService;
import xxd.demo.hystrix.vo.RecommendData;

import java.util.List;

/**
 * Created by xiedong
 * 2024/2/27
 */
@RestController
@Slf4j
@RequestMapping("/feed/api/v1")
public class FeedController {

    @Resource
    private RecommendService recommendService;

    @GetMapping("/recommend")
    public Object recommend(@RequestParam long userId) {
        long start = System.currentTimeMillis();
        List<RecommendData> recommenData = recommendService.getRecommenData(userId);
        long end = System.currentTimeMillis();
        log.info("recommend-cost:{}", end - start);
        return recommenData;
    }
}
