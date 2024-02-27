package xxd.demo.hystrix.outer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

/**
 * Created by xiedong
 * 2024/2/27
 */
@Slf4j
public abstract class BaseService {
    @Resource
    protected RestTemplate restTemplate;
}
