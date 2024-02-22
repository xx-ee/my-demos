package xxd.demos.mmap;

import cn.hutool.core.collection.CollUtil;
import xxd.demos.mmap.dto.StoreFeedData;
import xxd.demos.mmap.model.UserFeed;
import xxd.demos.mmap.mq.FromFeedConsumer;
import xxd.demos.mmap.outer.FollowService;
import xxd.demos.mmap.outer.FollowServiceImpl;
import xxd.demos.mmap.service.StoreService;
import xxd.demos.mmap.service.StoreServiceImpl;
import xxd.demos.mmap.utils.GsonUtil;

import java.util.*;

/**
 * Created by xiedong
 * Date: 2024/2/22 18:23
 */
public class PullFeedListTest {
    public static final StoreService storeService = new StoreServiceImpl();
    public static final FollowService followService = new FollowServiceImpl();
    public static final int pageNum = 10;


    public static void main(String[] args) {
        // test mq
        FromFeedConsumer fromFeedConsumer = new FromFeedConsumer();
        fromFeedConsumer.init(storeService, followService);

        List<UserFeed> result = new ArrayList<>();
        //lastId游标
        long lastId = System.currentTimeMillis();

        //1、获取关注的人数
        List<Long> followUserIdList = followService.getAllFollowUserIdList();


        //2、遍历关注的人数
        boolean today = storeService.getFeedIndexEndOfToday().getTime() > lastId;
        if (today) {
            for (Long followUserId : followUserIdList) {
                //查询对应发件箱
                List<StoreFeedData> allTodayFeedIndex = storeService.getTodayFeedIndexFromLastId(followUserId, lastId, pageNum);
                if (CollUtil.isEmpty(allTodayFeedIndex)) {
                    continue;
                }

                // join 结果集
                CollUtil.addAll(result, allTodayFeedIndex);
                CollUtil.sort(result, (o1, o2) -> o2.getFeedTime().compareTo(o1.getFeedTime()));
                result = CollUtil.sub(result, 0, pageNum);
            }
        }

        // 3、 check 条数是否满足
        if (CollUtil.size(result) >= pageNum) {
            System.out.println("first----ok");
            System.out.println(GsonUtil.toJson(result));
            return;
        }

        //4、查询历史
        long pageLastId = CollUtil.isEmpty(result) ? lastId : CollUtil.getLast(result).getFeedTime().getTime();
        int pageLeftNum = pageNum - CollUtil.size(result);
        for (Long followUserId : followUserIdList) {
            List<StoreFeedData> historyFeedIndexFromLastId = storeService.getHistoryFeedIndexFromLastId(followUserId, pageLastId, pageLeftNum);
            if (CollUtil.isEmpty(historyFeedIndexFromLastId)) {
                continue;
            }

            // join 结果集
            CollUtil.addAll(result, historyFeedIndexFromLastId);
            CollUtil.sort(result, (o1, o2) -> o2.getFeedTime().compareTo(o1.getFeedTime()));
            result = CollUtil.sub(result, 0, pageNum);
        }


        //5、返回结果
        System.out.println("second----ok");
        System.out.println(GsonUtil.toJson(result));
    }
}
