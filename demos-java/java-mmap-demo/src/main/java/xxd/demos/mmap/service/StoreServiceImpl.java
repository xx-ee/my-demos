package xxd.demos.mmap.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import xxd.demos.mmap.dto.HistoryFeedData;
import xxd.demos.mmap.dto.StoreFeedData;

import java.util.*;

/**
 * Created by xiedong
 * Date: 2024/2/22 18:43
 */
@Slf4j
public class StoreServiceImpl implements StoreService {
    private final Cache<Long, List<StoreFeedData>> allTodayFeedIndex;
    private final Cache<Long, List<HistoryFeedData>> historyFeedIndex;
    private Date indexStartDate;
    private Date indexEndDate;

    public StoreServiceImpl() {
        allTodayFeedIndex = Caffeine.newBuilder().build();
        historyFeedIndex = Caffeine.newBuilder().build();
        indexStartDate = DateUtil.beginOfDay(new Date());
        indexEndDate = DateUtil.endOfDay(new Date());
    }

    @Override
    public boolean storeFeed(StoreFeedData storeFeedData) {
        log.info("storeFeed-start,storeFeedData");
        long userId = storeFeedData.getUserId();

        List<StoreFeedData> todayFeedData = this.getAllTodayFeedIndex().getIfPresent(userId);
        if (Objects.isNull(todayFeedData)) {
            todayFeedData = new ArrayList<>();
        }
        todayFeedData.add(storeFeedData);

        CollUtil.sort(todayFeedData, (o1, o2) -> o2.getFeedTime().compareTo(o2.getFeedTime()));

        this.getAllTodayFeedIndex().put(userId, todayFeedData);

        return true;
    }

    @Override
    public Date getFeedIndexEndOfToday() {
        return this.indexEndDate;
    }

    @Override
    public Date getFeedIndexStartOfToday() {
        return this.indexStartDate;
    }

    @Override
    public void setFeedIndexEndOfToday(Date indexEndDate) {
        this.indexEndDate = indexEndDate;
    }

    @Override
    public void setFeedIndexStartOfToday(Date indexStartDate) {
        this.indexStartDate = indexStartDate;
    }

    @Override
    public Cache<Long, List<StoreFeedData>> getAllTodayFeedIndex() {
        return this.allTodayFeedIndex;
    }

    @Override
    public List<StoreFeedData> getTodayFeedIndexFromLastId(long userId) {
        return this.getAllTodayFeedIndex().getIfPresent(userId);
    }

    @Override
    public List<StoreFeedData> getTodayFeedIndexFromLastId(long userId, long lastId, int pageNum) {
        List<StoreFeedData> todayFeedIndex = this.getTodayFeedIndexFromLastId(userId);
        if (CollUtil.isEmpty(todayFeedIndex)) {
            return null;
        }
        // 如果找到了第一个大于等于 lastId 的元素，则从该索引开始复制列表
        int index = Collections.binarySearch(todayFeedIndex, new StoreFeedData() {{
            setFeedTime(lastId);
        }});
        return ListUtil.sub(todayFeedIndex, index, index + pageNum);
    }

    @Override
    public Cache<Long, List<HistoryFeedData>> getAllHistoryFeedIndex() {
        return this.historyFeedIndex;
    }

    @Override
    public List<HistoryFeedData> getHistoryFeedIndexFromLastId(long userId) {
        return this.getAllHistoryFeedIndex().getIfPresent(userId);
    }

    @Override
    public List<StoreFeedData> getHistoryFeedIndexFromLastId(long userId, long lastId, int pageNum) {
        List<HistoryFeedData> historyFeedIndex = this.getHistoryFeedIndexFromLastId(userId);
        if (CollUtil.isEmpty(historyFeedIndex)) {
            return null;
        }
        Date date = new Date(lastId);
        int year = DateUtil.year(date);
        int month = DateUtil.month(date);

        int index = Collections.binarySearch(historyFeedIndex, new HistoryFeedData() {{
            setYear((short) year);
            setMonth((byte) month);
        }});
        HistoryFeedData historyFeedData = historyFeedIndex.get(index);

        // 进行mmap查找 TODO:

        return null;
    }
}
