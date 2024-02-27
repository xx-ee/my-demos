package xxd.demo.hystrix.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by xiedong
 * 2024/2/27
 */
@Data
public class RecommendData implements Serializable {

    private String id;
    private NoteCard noteCard;

    @Data
    public static class NoteCard implements Serializable {
        private UserInfo userInfo;
        private Content content;
        private InteractInfo interactInfo;

        @Data
        public static class UserInfo implements Serializable {
            private long userId;
            private String nickName;
        }

        @Data
        public static class Content implements Serializable {
            private String contentId;
            private String displayTitle;
        }

        @Data
        public static class InteractInfo implements Serializable {
            private String likedCount;
            private boolean liked;
        }
    }

}
