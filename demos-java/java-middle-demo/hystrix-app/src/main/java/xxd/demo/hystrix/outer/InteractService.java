package xxd.demo.hystrix.outer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xxd.demos.hystrix.annotation.DoHystrix;

/**
 * Created by xiedong
 * 2024/2/27
 */
@Slf4j
@Service
public class InteractService extends BaseService {
    @DoHystrix(
            groupKey = "getLikeCountGroup",
            commandKey = "getLikeCount",
            threadPoolKey = "getLikeCountThreadPool",
            cacheKey = "#contentId", useCacheAfter = true)
    public String getLikeCount(String contentId) {
        String result = restTemplate.getForObject("http://10.2.8.102:8090/outer/interact/api/like/count/query?contentId=" + contentId, String.class);
        return result;
    }

    @DoHystrix(
            groupKey = "checkLikedGroup",
            commandKey = "checkLiked",
            threadPoolKey = "checkLikedThreadPool",
            cacheKey = "userId:#userId_contentId:#contentId", useCacheAfter = true)
    public boolean checkLiked(long userId, String contentId) {
        boolean result = Boolean.TRUE.equals(restTemplate.getForObject("http://10.2.8.102:8090/outer/interact/api/like/islike/query?userId=" + userId + "&contentId=" + contentId, Boolean.class));
        return result;
    }
}
