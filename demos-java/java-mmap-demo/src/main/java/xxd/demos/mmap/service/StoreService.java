package xxd.demos.mmap.service;

import com.github.benmanes.caffeine.cache.Cache;
import xxd.demos.mmap.dto.HistoryFeedData;
import xxd.demos.mmap.dto.StoreFeedData;

import java.util.Date;
import java.util.List;

/**
 * Created by xiedong
 * Date: 2024/2/22 18:28
 */
public interface StoreService {
    boolean storeFeed(StoreFeedData storeFeedData);

    Date getFeedIndexEndOfToday();

    void setFeedIndexEndOfToday(Date indexEndDate);

    Date getFeedIndexStartOfToday();

    void setFeedIndexStartOfToday(Date indexStartDate);

    /**
     * key: userId
     * value: feedId,feedTime,feedSetId
     *
     * @return
     */
    Cache<Long, List<StoreFeedData>> getAllTodayFeedIndex();

    List<StoreFeedData> getTodayFeedIndexFromLastId(long userId);

    List<StoreFeedData> getTodayFeedIndexFromLastId(long userId, long lastId, int pageNum);

    /**
     * key：userId
     * value : yyyy,MM,position,length
     *
     * @return
     */
    Cache<Long, List<HistoryFeedData>> getAllHistoryFeedIndex();

    List<HistoryFeedData> getHistoryFeedIndexFromLastId(long userId);

    List<StoreFeedData> getHistoryFeedIndexFromLastId(long userId, long lastId, int pageNum);
}
