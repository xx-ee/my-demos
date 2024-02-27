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
public class ContentService extends BaseService {

    @DoHystrix(
            groupKey = "getContentTitleGroup",
            commandKey = "getContentTitle",
            threadPoolKey = "getContentTitleThreadPool",
            cacheKey = "#contentId", useCacheAfter = true)
    public String getContentTitle(String contentId) {
        String result = restTemplate.getForObject("http://10.2.8.102:8090/outer/content/api/content/titile/query?contentId=" + contentId, String.class);
        return result;
    }
}
