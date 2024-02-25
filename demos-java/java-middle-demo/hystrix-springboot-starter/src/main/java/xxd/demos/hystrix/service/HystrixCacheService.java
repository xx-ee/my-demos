package xxd.demos.hystrix.service;

/**
 * Created by xiedong
 * Date: 2024/2/25 16:27
 */
public interface HystrixCacheService {
    Object getFallbackData(String key);

    void putFallBackData(String key, Object val);


    int getExeTimeout(String commandKey);

    void putExeTimeout(String commandKey, int exeTimeout);

    void putForceOpen(String commandKey, boolean forceOpen);

    boolean getForceOpen(String commandKey);
}
