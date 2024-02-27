package xxd.demo.hystrix.outer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import xxd.demos.hystrix.annotation.DoHystrix;

/**
 * Created by xiedong
 * 2024/2/27
 */
@Service
@Slf4j
public class UserInfoOuterService {
    @Resource
    private RestTemplate restTemplate;

    @DoHystrix(
            groupKey = "getNicknameGroup",
            commandKey = "getNickname",
            threadPoolKey = "getNicknameThreadPool",
            cacheKey = "#contentId", useCacheFirst = true)
    public String getNickname(long userId) {
        String result = restTemplate.getForObject("http://10.2.8.102:8090/outer/user/api/nickname/query?userId=" + userId, String.class);
        return result;
    }

}
