package xxd.demos.hystrix.service;

/**
 * Created by xiedong
 * Date: 2024/2/25 16:27
 */
public interface HystrixCacheService {
    String getFallbackData(String key);

    void putFallBackData(String key, Object val);
}
