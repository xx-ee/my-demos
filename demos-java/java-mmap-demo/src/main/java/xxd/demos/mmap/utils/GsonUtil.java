package xxd.demos.mmap.utils;

import com.google.gson.Gson;
import lombok.experimental.UtilityClass;

/**
 * Created by xiedong
 * Date: 2024/2/22 18:54
 */
@UtilityClass
public class GsonUtil {
    private static final Gson gson = new Gson();

    public String toJson(Object obj) {
        return gson.toJson(obj);
    }
}
