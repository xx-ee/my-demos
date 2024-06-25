package xxd.demo.hystrix;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import io.github.xxee.hystrix.cache.annotation.EnableHystrixCmd;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableHystrixCmd
@EnableMethodCache(basePackages = "xxd.**")
@EnableCreateCacheAnnotation // deprecated in jetcache 2.7, can be removed if @CreateCache is not used
public class HystrixAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixAppApplication.class, args);
    }

}
