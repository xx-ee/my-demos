package xxd.demos.mmap.store;

import lombok.Data;
import lombok.ToString;

/**
 * Created by xiedong
 * Date: 2024/2/22 17:27
 */
@ToString
@Data
public class AppendFeedResult {
    // Return code
    private AppendMessageStatus status;
    // Where to start writing
    private long wroteOffset;
    // Write Bytes
    private int wroteBytes;


    public AppendFeedResult(AppendMessageStatus status) {
        this(status, 0, 0);
    }

    public AppendFeedResult(AppendMessageStatus status, long wroteOffset, int wroteBytes) {
        this.status = status;
        this.wroteOffset = wroteOffset;
        this.wroteBytes = wroteBytes;
    }
}
