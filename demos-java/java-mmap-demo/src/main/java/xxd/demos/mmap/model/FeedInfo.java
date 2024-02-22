package xxd.demos.mmap.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Created by xiedong
 * Date: 2024/2/22 18:26
 */
@Data
@ToString
public class FeedInfo {
    private long id;
    private long userId;
    private int type;
    private String content;
    private String sourceId;
    private int status;
    private long feedSetId;
    private Date feedTime;
}
