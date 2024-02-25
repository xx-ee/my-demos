package xxd.demos.hystrix.aspect;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xxd.demos.hystrix.annotation.DoHystrix;
import xxd.demos.hystrix.command.DoHystrixCommand;
import xxd.demos.hystrix.service.HystrixCacheService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.Duration;

/**
 * Created by xiedong
 * Date: 2024/2/24 22:39
 */
@Aspect
@Component
public class DoHystrixAspect {

    @Resource
    private HystrixCacheService hystrixCacheService;

    @Pointcut("@annotation(xxd.demos.hystrix.annotation.DoHystrix)")
    public void aopPoint() {
    }

    @Around("aopPoint() && @annotation(doGovern)")
    public Object doRouter(ProceedingJoinPoint jp, DoHystrix doGovern) throws Throwable {
        DoHystrixCommand doHystrixCommand = new DoHystrixCommand(doGovern);
        return doHystrixCommand.access(jp, getMethod(jp), jp.getArgs(), hystrixCacheService);
    }

    private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }

}
