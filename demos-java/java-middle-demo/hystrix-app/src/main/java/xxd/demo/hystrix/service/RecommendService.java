package xxd.demo.hystrix.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xxd.demo.hystrix.outer.ContentService;
import xxd.demo.hystrix.outer.InteractService;
import xxd.demo.hystrix.outer.UserInfoService;
import xxd.demo.hystrix.vo.RecommendData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiedong
 * 2024/2/27
 */
@Service
@Slf4j
public class RecommendService {

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private ContentService contentService;
    @Resource
    private InteractService interactService;


    public List<RecommendData> getRecommenData(long userId) {
        //1、取原始feed列表（未聚合信息）
        List<RecommendData> dataList = this.getInitData(userId);

        //2、聚合信息
        this.aggregateData(dataList);

        return dataList;
    }

    private void aggregateData(List<RecommendData> dataList) {


        for (RecommendData data : dataList) {
            RecommendData.NoteCard noteCard = data.getNoteCard();
            //聚合源信息
            String contentId = noteCard.getContent().getContentId();
            noteCard.getContent().setDisplayTitle(contentService.getContentTitle(contentId));

            //聚合用户信息
            long userId = noteCard.getUserInfo().getUserId();
            noteCard.getUserInfo().setNickName(userInfoService.getNickname(userId));

            //聚合互动信息
            noteCard.getInteractInfo().setLikedCount(interactService.getLikeCount(contentId));
            noteCard.getInteractInfo().setLiked(interactService.checkLiked(userId, contentId));
        }
    }

    private List<RecommendData> getInitData(long userId) {
        List<RecommendData> initData = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            RecommendData recommendData = new RecommendData();
            RecommendData.NoteCard noteCard = new RecommendData.NoteCard();

            //发布者用户id
            RecommendData.NoteCard.UserInfo userInfo = new RecommendData.NoteCard.UserInfo();
            userInfo.setUserId(i * 1000);

            //发布内容id
            RecommendData.NoteCard.Content content = new RecommendData.NoteCard.Content();
            content.setContentId(i * 2000 + "");


            noteCard.setUserInfo(userInfo);
            noteCard.setContent(content);
            noteCard.setInteractInfo(new RecommendData.NoteCard.InteractInfo());
            recommendData.setNoteCard(noteCard);
            recommendData.setId(IdUtil.fastUUID());
            initData.add(recommendData);
        }
        return initData;
    }


}
