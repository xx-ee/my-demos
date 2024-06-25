package xxd.demos.hystrix;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import com.netflix.hystrix.Hystrix;
import com.soundcloud.prometheus.hystrix.HystrixPrometheusMetricsPublisher;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xxd.demos.hystrix.aspect.DoHystrixAspect;
import xxd.demos.hystrix.command.DoHystrixCommand;
import xxd.demos.hystrix.listener.HystrixPropertiesListener;
import xxd.demos.hystrix.service.HystrixCacheService;
import xxd.demos.hystrix.service.impl.HystrixCacheServiceImpl;

import java.time.Duration;

/**
 * Created by xiedong
 * 2024/2/26
 */
@Configuration
@ConditionalOnClass(value = {
        DoHystrixCommand.class
})
@ConditionalOnBean(CacheManager.class)
@Slf4j
public class DoHystrixAutoConfiguration {
    public static final String HystrixCache = "hystrixCache";

    @Bean
    @ConditionalOnMissingBean
    public HystrixCacheService hystrixCacheService(@Autowired @Qualifier(value = HystrixCache)
                                                   Cache<String, Object> hystrixCache) {
        return new HystrixCacheServiceImpl(hystrixCache);
    }

    @Bean(name = HystrixCache, destroyMethod = "close")
    @ConditionalOnMissingBean
    public Cache<String, Object> getHystrixCache(@Autowired CacheManager cacheManager) {
        QuickConfig qc = QuickConfig.newBuilder(HystrixCache)
                .expire(Duration.ofSeconds(5))
                .cacheType(CacheType.LOCAL) // two level cache
                .localLimit(50)
                .syncLocal(true) // invalidate local cache in all jvm process after update
                .build();
        return cacheManager.getOrCreateCache(qc);
    }


    @Bean
    public DoHystrixAspect doHystrixAspect(@Autowired HystrixCacheService hystrixCacheService) {
        return new DoHystrixAspect(hystrixCacheService);
    }

    @Bean
    public HystrixShutdownHook hystrixShutdownHook() {
        return new HystrixShutdownHook();
    }

    private class HystrixShutdownHook implements DisposableBean {
        private HystrixShutdownHook() {
        }

        public void destroy() throws Exception {
            com.netflix.hystrix.Hystrix.reset();
        }
    }

    @Bean
    @ConditionalOnClass(value = {
            PrometheusMeterRegistry.class,
            DoHystrixCommand.class
    })
    public HystrixPrometheus hystrixPrometheus(@Autowired PrometheusMeterRegistry prometheusMeterRegistry) {
        return new HystrixPrometheus(prometheusMeterRegistry);
    }


    private class HystrixPrometheus {
        private HystrixPrometheus(PrometheusMeterRegistry prometheusMeterRegistry) {
            HystrixPrometheusMetricsPublisher.builder()
                    .shouldExportProperties(true)
                    .shouldExportDeprecatedMetrics(true)
                    .withRegistry(prometheusMeterRegistry.getPrometheusRegistry()).buildAndRegister();
            log.info("hystrix prometheus adaptored...");
        }
    }

    @Bean
    public HystrixPropertiesListener hystrixPropertiesListener() {
        return new HystrixPropertiesListener();
    }

}
