//package xxd.demos.hystrix.controller;
//
//import com.netflix.config.AbstractPollingScheduler;
//import com.netflix.config.ConfigurationManager;
//import com.netflix.config.DynamicConfiguration;
//import com.netflix.config.FixedDelayPollingScheduler;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Created by xiedong
// * Date: 2024/2/25 19:49
// */
////@Configuration
//public class DegradeDynamicConfig {
//
//    @Bean
//    public DynamicConfiguration dynamicConfiguration(@Autowired DegradeConfigSource degradeConfigSource) {
//        AbstractPollingScheduler scheduler = new FixedDelayPollingScheduler(5* 1000, 2 * 1000, false);
//        DynamicConfiguration configuration = new DynamicConfiguration(degradeConfigSource, scheduler);
//        ConfigurationManager.install(configuration); // must install to enable configuration
//        return configuration;
//    }
//
//}
