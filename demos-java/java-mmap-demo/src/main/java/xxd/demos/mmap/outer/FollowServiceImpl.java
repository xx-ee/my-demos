package xxd.demos.mmap.outer;

import cn.hutool.core.util.RandomUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xiedong
 * Date: 2024/2/22 19:19
 */
public class FollowServiceImpl implements FollowService {
    @Override
    public List<Long> getAllFollowUserIdList() {
        return Arrays.asList(123L, 124L, 125L);
    }

    @Override
    public Long getRandomFollowUserId() {
        return RandomUtil.randomEle(this.getAllFollowUserIdList());
    }
}
