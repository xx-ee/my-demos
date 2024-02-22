package xxd.demos.mmap.mq;

import lombok.extern.slf4j.Slf4j;
import xxd.demos.mmap.dto.StoreFeedData;
import xxd.demos.mmap.model.FeedInfo;
import xxd.demos.mmap.outer.FollowService;
import xxd.demos.mmap.service.StoreService;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiedong
 * Date: 2024/2/22 18:22
 */
@Slf4j
public class FromFeedConsumer {

    public void init(StoreService storeService, FollowService followService) {
        log.info("fromFeedConsumer-init...");
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> {
            try {
                Long userId = followService.getRandomFollowUserId();
                long currentTimeMillis = System.currentTimeMillis();
                FeedInfo feedInfo = new FeedInfo();
                feedInfo.setId(currentTimeMillis);
                feedInfo.setUserId(userId);
                feedInfo.setType(1);
                feedInfo.setContent("test" + currentTimeMillis);
                feedInfo.setSourceId(currentTimeMillis + "");
                feedInfo.setStatus(0);
                feedInfo.setFeedSetId(0);
                feedInfo.setFeedTime(new Date(currentTimeMillis));

                //store
                StoreFeedData storeFeedData = new StoreFeedData();
                storeFeedData.setFeedId(feedInfo.getId());
                storeFeedData.setUserId(feedInfo.getUserId());
                storeFeedData.setFeedTime(feedInfo.getFeedTime().getTime());
                storeFeedData.setFeedSetId(feedInfo.getFeedSetId());
                storeService.storeFeed(storeFeedData);
                log.info("storeService,store success:{}", currentTimeMillis);
            } catch (Exception e) {
            }
        }, 0, 3000L, TimeUnit.SECONDS);

    }
}
