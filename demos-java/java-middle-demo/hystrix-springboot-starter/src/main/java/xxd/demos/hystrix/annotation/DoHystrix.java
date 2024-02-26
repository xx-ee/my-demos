package xxd.demos.hystrix.annotation;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xiedong
 * Date: 2024/2/24 22:38
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DoHystrix {
    String cachePrefix() default "default";

    String cacheKey() default "";

    boolean enableCache() default true;

    boolean useCacheFirst() default false;

    boolean useCacheAfter() default false;

    /**
     * The command group key is used for grouping together commands such as for reporting,
     * alerting, dashboards or team/library ownership.
     * <p/>
     * default => the runtime class name of annotated method
     *
     * @return group key
     */
    String groupKey() default "";

    /**
     * Hystrix command key.
     * <p/>
     * default => the name of annotated method. for example:
     * <code>
     * ...
     *
     * @return command key
     * @HystrixCommand public User getUserById(...)
     * ...
     * the command name will be: 'getUserById'
     * </code>
     */
    String commandKey() default "";

    /**
     * The thread-pool key is used to represent a
     * HystrixThreadPool for monitoring, metrics publishing, caching and other such uses.
     *
     * @return thread pool key
     */
    String threadPoolKey() default "";

}
