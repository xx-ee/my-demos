package xxd.demos.mmap.store;

import lombok.Data;

/**
 * Created by xiedong
 * Date: 2024/2/22 17:27
 */
@Data
public class Message {
    private long userId;
    private long feedId;
    private long feedTime;
    private long feedSetId;
    private byte feedStatus;
}
