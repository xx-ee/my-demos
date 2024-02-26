package xxd.demos.hystrix.config;

import com.soundcloud.prometheus.hystrix.HystrixPrometheusMetricsPublisher;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import xxd.demos.hystrix.command.DoHystrixCommand;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by xiedong
 * 2024/2/26
 */
@Configuration
@ConditionalOnClass(value = {
        PrometheusMeterRegistry.class,
        DoHystrixCommand.class
})
@Slf4j
public class HystrixPrometheusAutoConfiguration {
    @Resource
    private PrometheusMeterRegistry prometheusMeterRegistry;

    @PostConstruct
    public void init() {
        HystrixPrometheusMetricsPublisher.builder().withRegistry(prometheusMeterRegistry.getPrometheusRegistry()).buildAndRegister();
        log.info("hystrix prometheus adaptored...");
    }
}
