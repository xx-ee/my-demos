package xxd.demos.hystrix.controller;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import xxd.demos.hystrix.service.UserService;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 测试：http://localhost:8081/api/queryUserInfo?userId=aaa
     */
//    @MyHystrix(timeoutValue = 350, returnJson = "{\"code\":\"1111\",\"info\":\"调用方法超过350毫秒，熔断返回！\"}")
    @RequestMapping(path = "/api/queryUserInfo", method = RequestMethod.GET)
    @HystrixCommand(
            groupKey = "testGroup",
            commandKey = "testCommand",
            threadPoolKey = "testThreadPool",
            fallbackMethod = "testFallBack"
    )
    public UserInfo queryUserInfo(@RequestParam String userId) throws InterruptedException {
        logger.info("查询用户信息，userId：{}", userId);
        int a = 1 / 0;
        Thread.sleep(1000);
        return new UserInfo("虫虫:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");
    }

    public UserInfo testFallBack(@RequestParam String userId, Throwable a) {
        return new UserInfo("虫虫1111:" + userId, 22, "fallback" + a.getMessage());
    }





//    @GetMapping("getRemote")
//    @Cached(name="userCache:", key = "#id", expire = 3600, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.REMOTE)
//    public User getRemote(Long id){
//        // 直接新建用户，模拟从数据库获取数据
//        User user = new User();
//        user.setId(id);
//        user.setName("用户remote"+id);
//        user.setAge(23);
//        user.setSex(1);
//        System.out.println("第一次获取数据，未走缓存："+id);
//        return user;
//    }
    @Resource
    private UserService userService;
    @GetMapping("getLocal")
    public User getLocal(Long id){
       return userService.getLocal(id);
    }

    @GetMapping("getBoth")
    @Cached(name="userCache:", key = "#id", expire = 3600, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.BOTH)
    public User getBoth(Long id){
        // 直接新建用户，模拟从数据库获取数据
        User user = new User();
        user.setId(id);
        user.setName("用户both"+id);
        user.setAge(23);
        user.setSex(1);
        System.out.println("第一次获取数据，未走缓存："+id);
        return user;
    }

    @PostMapping("updateUser")
    @CacheUpdate(name = "userCache:", key = "#user.id", value = "#user")
    public Boolean updateUser(@RequestBody User user){
        // TODO 更新数据库
        return true;
    }

    @PostMapping("deleteUser")
    @CacheInvalidate(name = "userCache:", key = "#id")
    public Boolean deleteUser(Long id){
        // TODO 从数据库删除
        return true;
    }

}
