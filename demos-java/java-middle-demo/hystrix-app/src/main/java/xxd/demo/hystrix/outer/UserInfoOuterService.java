package xxd.demo.hystrix.outer;

import io.github.xxee.hystrix.cache.annotation.HystrixCmd;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by xiedong
 * 2024/2/27
 */
@Service
@Slf4j
public class UserInfoOuterService {
    @Resource
    private RestTemplate restTemplate;

    // 1、正常
//    @HystrixCmd(groupKey = "getNicknameGroup", commandKey = "getNickname", threadPoolKey = "getNicknameThreadPool")

    // 2、优先使用缓存模式
//    @HystrixCmd(groupKey = "getNicknameGroup", commandKey = "getNickname", threadPoolKey = "getNicknameThreadPool", cacheKey = "#userId", useCacheFirst = true)

    // 3、兜底使用缓存模式--兜底成功
//    @HystrixCmd(groupKey = "getNicknameGroup", commandKey = "getNickname", threadPoolKey = "getNicknameThreadPool", cacheKey = "#userId", useCacheAfter = true)


    //4、兜底使用缓存模式--兜底失效返回默认数据
    @HystrixCmd(groupKey = "getNicknameGroup", commandKey = "getNickname", threadPoolKey = "getNicknameThreadPool", cacheKey = "#userId", useCacheAfter = true, fallbackDefaultJson = "{\n" + "  \"key1\": \"11\",\n" + "  \"key2\": \"这是默认返回数据\"\n" + "}")
    public String getNickname(long userId) {
        String result = restTemplate.getForObject("http://localhost:8888/user?userId=" + userId, String.class);
//        String result = restTemplate.getForObject("http://localhost:" + serverPort + "/outer/user/api/nickname/query?userId=" + userId, String.class);
        return result;
    }

}
