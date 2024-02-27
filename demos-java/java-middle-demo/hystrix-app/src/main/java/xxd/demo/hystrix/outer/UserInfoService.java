package xxd.demo.hystrix.outer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xxd.demos.hystrix.annotation.DoHystrix;

/**
 * Created by xiedong
 * 2024/2/27
 */
@Service
@Slf4j
public class UserInfoService extends BaseService {

    @DoHystrix(
            groupKey = "getNicknameGroup",
            commandKey = "getNickname",
            threadPoolKey = "getNicknameThreadPool",
            cacheKey = "#contentId", useCacheAfter = true)
    public String getNickname(long userId) {
        String result = restTemplate.getForObject("http://10.2.8.102:8090/outer/user/api/nickname/query?userId=" + userId, String.class);
        return result;
    }

}
