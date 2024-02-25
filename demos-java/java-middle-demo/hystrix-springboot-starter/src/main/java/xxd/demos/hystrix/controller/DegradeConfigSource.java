//package xxd.demos.hystrix.controller;
//
//import com.netflix.config.PollResult;
//import com.netflix.config.PolledConfigurationSource;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.*;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.stereotype.Component;
//
//
//import javax.annotation.Resource;
//import java.util.*;
//
///**
// * Created by xiedong
// * Date: 2024/2/25 19:48
// */
//@Configuration
//public class DegradeConfigSource implements PolledConfigurationSource {
//    public final String HystrixPrefix = "hystrix";
//
//    //spring的Environment
//    private ConfigurableEnvironment environment;
//    private PropertySourcesPropertyResolver resolver;
//
//    DegradeConfigSource(ConfigurableEnvironment environment) {
//        this.environment = environment;
//        this.resolver = new PropertySourcesPropertyResolver(this.environment.getPropertySources());
//        this.resolver.setIgnoreUnresolvableNestedPlaceholders(true);
//
//    }
//
//    @Override
//    public PollResult poll(boolean initial, Object checkPoint) throws Exception {
//        //Map<String, Object> complete = getHystrixConfig();
//        Map<String, Object> complete = getProperties();
//        return PollResult.createFull(complete);
//    }
//
//    @Resource
//    private ResourceLoader resourceLoader;
//    /**
//     * @Author wangyunfei
//     * @Description 获取配置文件中的指定hystrix前缀信息
//     * 缺点：1）不支持yml格式 只支持properties
//     *       2）只能读取某一个配置文件中的信息 如果配置是写在不同配置文件则不支持
//     * @Date 2019/12/20 14:58
//     * @Param []
//     * @return java.util.HashMap<java.lang.String,java.lang.String>
//     **/
//    public HashMap<String,Object> getHystrixConfig() throws Exception
//    {
//        HashMap<String,Object> hystrixMap = new  HashMap<String,Object>();
//        org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:application-mod.properties");
//        Properties props = new Properties();
//        props.load(resource.getInputStream());
//        Enumeration keys = props.propertyNames();
//        while (keys.hasMoreElements()) {
//            String key = (String) keys.nextElement();
//            System.out.println(key + "=" + props.getProperty(key));
//            if(key.contains(HystrixPrefix))
//            {
//                hystrixMap.put(key, props.getProperty(key));
//            }
//        }
//        return hystrixMap;
//    }
//
//    /**
//     * @Author wangyunfei
//     * @Description 获取所有配置信息
//     * @Date 2019/12/20 15:06
//     * @Param []
//     * @return java.util.Map<java.lang.String,java.lang.Object>
//     **/
//    public Map<String, Object> getProperties() {
//        Map<String, Object> properties = new LinkedHashMap<>();
//        //spring env 里面也是多个source组合的
//        for (Map.Entry<String, PropertySource<?>> entry : getPropertySources().entrySet()) {
//            PropertySource<?> source = entry.getValue();
//            if (source instanceof EnumerablePropertySource) {
//                EnumerablePropertySource<?> enumerable = (EnumerablePropertySource<?>) source;
//                for (String name : enumerable.getPropertyNames()) {
//                    if (!properties.containsKey(name) && name.contains(HystrixPrefix)) {
//                        properties.put(name, resolver.getProperty(name));
//                    }
//                }
//            }
//        }
//        return properties;
//    }
//
//    //PropertySource也可能是组合的，通过递归获取
//    private Map<String, PropertySource<?>> getPropertySources() {
//        Map<String, PropertySource<?>> map = new LinkedHashMap<String, PropertySource<?>>();
//        MutablePropertySources sources = null;
//        if (environment != null) {
//            sources = environment.getPropertySources();
//        } else {
//            sources = new StandardEnvironment().getPropertySources();
//        }
//        for (PropertySource<?> source : sources) {
//            extract("", map, source);
//        }
//        return map;
//    }
//
//    private void extract(String root, Map<String, PropertySource<?>> map,
//                         PropertySource<?> source) {
//        if (source instanceof CompositePropertySource) {
//            for (PropertySource<?> nest : ((CompositePropertySource) source)
//                    .getPropertySources()) {
//                extract(source.getName() + ":", map, nest);
//            }
//        } else {
//            map.put(root + source.getName(), source);
//        }
//    }
//}
