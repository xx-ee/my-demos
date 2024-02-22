package xxd.demos.mmap.outer;

import java.util.List;

/**
 * Created by xiedong
 * Date: 2024/2/22 19:14
 */
public interface FollowService {
    List<Long> getAllFollowUserIdList();

    Long getRandomFollowUserId();
}
