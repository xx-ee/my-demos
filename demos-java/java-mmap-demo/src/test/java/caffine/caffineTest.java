package caffine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiedong
 * Date: 2024/2/22 19:22
 */
public class caffineTest {
    public static void main(String[] args) {
        Cache<Long, List<Long>> caffeine = Caffeine.newBuilder().build();

    }
}
