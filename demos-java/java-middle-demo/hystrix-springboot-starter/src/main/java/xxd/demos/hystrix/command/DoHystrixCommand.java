package xxd.demos.hystrix.command;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import xxd.demos.hystrix.annotation.DoHystrix;
import xxd.demos.hystrix.service.HystrixCacheService;

import java.lang.reflect.Method;

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

        return this.execute();
    }

    @Override
    protected Object run() throws Exception {
        try {
            Object result = jp.proceed();
            //记录fallbackData
            this.hystrixCacheService.putFallBackData(doHystrix.cacheKey(), result);
            return result;
        } catch (Throwable e) {
            log.error("run-error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Object getFallback() {
        log.info("getFallback...");
        String s = this.hystrixCacheService.getFallbackData(doHystrix.cacheKey());
        return StringUtils.isBlank(s) ? null : JSON.parseObject(s, method.getReturnType());
    }

}

