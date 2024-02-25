package xxd.demos.hystrix.command;

import cn.hutool.extra.expression.ExpressionUtil;
import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import xxd.demos.hystrix.annotation.DoHystrix;
import xxd.demos.hystrix.service.HystrixCacheService;
import xxd.demos.hystrix.util.SpUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by xiedong
 * Date: 2024/2/24 22:51
 */
@Slf4j
public class DoHystrixCommand extends HystrixCommand<Object> {
    private ProceedingJoinPoint jp;
    private Method method;
    private DoHystrix doHystrix;
    private HystrixCacheService hystrixCacheService;
    private String cacheKey;

    public DoHystrixCommand(DoHystrix doHystrix) {
        /*********************************************************************************************
         * 置HystrixCommand的属性
         * GroupKey：            该命令属于哪一个组，可以帮助我们更好的组织命令。
         * CommandKey：          该命令的名称
         * ThreadPoolKey：       该命令所属线程池的名称，同样配置的命令会共享同一线程池，若不配置，会默认使用GroupKey作为线程池名称。
         * CommandProperties：   该命令的一些设置，包括断路器的配置，隔离策略，降级设置，以及一些监控指标等。
         * ThreadPoolProperties：关于线程池的配置，包括线程池大小，排队队列的大小等
         *********************************************************************************************/
        super(Setter.
                //设置groupKey
                        withGroupKey(HystrixCommandGroupKey.Factory.asKey(doHystrix.groupKey()))
                //设置commandKey
                .andCommandKey(HystrixCommandKey.Factory.asKey(doHystrix.commandKey()))
                //设置threadpoolKey
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(doHystrix.threadPoolKey()))
                //命令配置
                .andCommandPropertiesDefaults(HystrixCommandProperties
                        .Setter()
                        //设置隔离策略为线程池隔离
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                        //设置执行超时时间--// 设置熔断超时时间
                        .withExecutionTimeoutInMilliseconds(200))
                //线程池参数配置
                .andThreadPoolPropertiesDefaults(
                        HystrixThreadPoolProperties
                                //设置核心数为10
                                .Setter()
                                .withCoreSize(10))
        );
        this.doHystrix = doHystrix;
    }

    public Object access(ProceedingJoinPoint jp, Method method, Object[] args, HystrixCacheService hystrixCacheService) {
        this.jp = jp;
        this.method = method;
        this.hystrixCacheService = hystrixCacheService;
        // 设置熔断超时时间
//        Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(this.doHystrix.groupKey()))
//                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
//                        .withExecutionTimeoutInMilliseconds(doHystrix.timeoutValue()));
        this.cacheKey = generateCacheKey(method, args);
        return this.execute();
    }

    private String generateCacheKey(Method method, Object[] args) {
        try {
            if (this.doHystrix.enableCache()) {
                Object key = ExpressionUtil.eval(doHystrix.cacheKey(), SpUtil.getParamMap(method, args));
                return Objects.isNull(key) ? "" : this.doHystrix.cachePrefix() + key.toString();
            }
        } catch (Exception e) {
        }
        return "";
    }

    @Override
    protected Object run() throws Exception {
        try {
            if (this.doHystrix.enableCache() && this.doHystrix.useCacheFirst()) {
                Object result = this.hystrixCacheService.getFallbackData(this.cacheKey);
                if (result != null) {
                    log.info("hystrix-run use cache first...");
                    return JSON.parseObject(result.toString(), method.getReturnType());
                }
            }
            //
            Object result = jp.proceed();

            //记录fallbackData
            this.hystrixCacheService.putFallBackData(this.cacheKey, result);
            return result;
        } catch (Throwable e) {
            log.error("run-error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Object getFallback() {
        log.info("getFallback...key:{}", this.cacheKey);
        if (this.doHystrix.enableCache() && this.doHystrix.useCacheAfter()) {
            Object s = this.hystrixCacheService.getFallbackData(this.cacheKey);
            if (s != null) {
                return JSON.parseObject(s.toString(), method.getReturnType());
            }
        }
        return null;
    }

}

