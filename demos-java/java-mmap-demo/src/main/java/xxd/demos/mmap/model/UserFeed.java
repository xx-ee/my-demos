package xxd.demos.mmap.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Created by xiedong
 * Date: 2024/2/22 18:25
 */
@Data
@ToString
public class UserFeed {
    private long userId;
    private long feedUserId;
    private long feedId;
    private Date feedTime;
    private int feedType;
}
