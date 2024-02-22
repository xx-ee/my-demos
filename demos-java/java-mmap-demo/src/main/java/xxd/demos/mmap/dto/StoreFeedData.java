package xxd.demos.mmap.dto;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;

/**
 * Created by xiedong
 * Date: 2024/2/22 18:59
 */
@Data
public class StoreFeedData implements Comparable<StoreFeedData> {
    private long feedId;
    private long userId;
    private Long feedTime;
    private long feedSetId;

    @Override
    public int compareTo(StoreFeedData o) {
        return NumberUtil.compare(o.getFeedTime(), this.getFeedTime());
    }
}
