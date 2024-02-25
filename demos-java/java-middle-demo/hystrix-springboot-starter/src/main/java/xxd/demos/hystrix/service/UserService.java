package xxd.demos.hystrix.service;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import xxd.demos.hystrix.controller.User;
import xxd.demos.hystrix.controller.UserInfo;

import java.util.concurrent.TimeUnit;

/**
 * Created by xiedong
 * Date: 2024/2/25 10:32
 */
@Service
public class UserService {
    //    @Cached(name = "userCache:", key = "#id", expire = 3600, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.LOCAL)
    @HystrixCommand(
            groupKey = "testGroup",
            commandKey = "testCommand",
            threadPoolKey = "testThreadPool",
            fallbackMethod = "getLocalFallBack"
    )
    public User getLocal(Long id) {
        int a = 10 / 0;
        // 直接新建用户，模拟从数据库获取数据
        User user = new User();
        user.setId(id);
        user.setName("用户local" + id);
        user.setAge(23);
        user.setSex(1);
        System.out.println("第一次获取数据，未走缓存：" + id);
        return user;
    }


    public User getLocalFallBack(Long id, Throwable a) {
        // 直接新建用户，模拟从数据库获取数据
        User user = new User();
        user.setId(id);
        user.setName("用户local-fallback" + id);
        user.setAge(23);
        user.setSex(1);
        System.out.println("第一次获取数据，未走缓存：" + id);
        return user;
    }
}
