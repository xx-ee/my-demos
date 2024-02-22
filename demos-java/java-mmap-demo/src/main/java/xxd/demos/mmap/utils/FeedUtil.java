package xxd.demos.mmap.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import xxd.demos.mmap.dto.StoreFeedData;
import xxd.demos.mmap.model.FeedInfo;
import xxd.demos.mmap.model.UserFeed;

import java.util.Date;

/**
 * Created by xiedong
 * Date: 2024/2/22 18:37
 */
@UtilityClass
@Slf4j
public class FeedUtil {

    public FeedInfo convert(StoreFeedData todayFeedData) {
        // 1、查询redis  or  mysql
        FeedInfo feedInfo = new FeedInfo();
        feedInfo.setId(todayFeedData.getFeedId());
        feedInfo.setUserId(todayFeedData.getUserId());
        feedInfo.setFeedTime(new Date(todayFeedData.getFeedTime()));
        return feedInfo;
    }

    public UserFeed convert(long userId, StoreFeedData todayFeedData) {
        UserFeed userFeed = new UserFeed();
        userFeed.setUserId(userId);
        userFeed.setFeedUserId(todayFeedData.getUserId());
        userFeed.setFeedTime(new Date(todayFeedData.getFeedTime()));
        userFeed.setFeedId(todayFeedData.getFeedId());
        return userFeed;
    }
}
