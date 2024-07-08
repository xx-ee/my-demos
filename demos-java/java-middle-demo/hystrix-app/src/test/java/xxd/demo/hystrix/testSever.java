package xxd.demo.hystrix;

import cn.hutool.core.lang.Console;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpUtil;

import java.util.Date;

/**
 * Created by xiedong
 * 2024/6/26
 */
public class testSever {
    public static void main(String[] args) {
        HttpUtil.createServer(8888).addAction("/user", (req, res) -> {


            String userId = req.getParam("userId");
            Console.log("【{}】Http Server recevie userId:{}", new Date(), userId);
            try {
                Thread.sleep(Long.parseLong(userId));
            } catch (InterruptedException e) {
            }


            String content = "返回用户信息,用户id：" + userId;

            res.write(content, ContentType.JSON.toString());
        }).start();
    }
}
