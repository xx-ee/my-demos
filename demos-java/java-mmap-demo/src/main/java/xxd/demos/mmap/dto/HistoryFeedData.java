package xxd.demos.mmap.dto;

import lombok.Data;

/**
 * Created by xiedong
 * Date: 2024/2/22 18:33
 */
@Data
public class HistoryFeedData implements Comparable<HistoryFeedData> {
    private short year;
    private byte month;
    private long position;
    private long length;

    @Override
    public int compareTo(HistoryFeedData o) {
        // 先按照 year 排序，降序
        int yearCompare = Short.compare(o.year, this.year);
        if (yearCompare != 0) {
            return yearCompare;
        }
        // 如果 year 相同，则按照 month 排序，降序
        return Byte.compare(o.month, this.month);
    }
}
