import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.*;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CSVReaderExample {

    public static void main(String[] args) throws IOException {
        // CSV文件路径


        // 用于存储每个月的用户ID的Set
        Map<String, Set<String>> monthlyUsers = new HashMap<>();
        Map<String, Integer> monthlyTotal = new HashMap<>();
        File[] ls = FileUtil.ls("D:\\tmp");

        TimeInterval timer = DateUtil.timer();
        long total = 0;
        for (File csvFile : ls) {
            String name = csvFile.getName();

            System.out.println("start file:" + name);

            CsvReader reader = CsvUtil.getReader();
//从文件中读取CSV数据
            CsvData data = reader.read(csvFile);
            int size = data.getRows().size();

            System.out.println("fileSize:" + name + "," + size);
            total += size;
            // 遍历CSV数据行
            for (CsvRow row : data.getRows()) {
                String dateString = row.get(2); // 第三列是日期
                if (StrUtil.isNotBlank(dateString)) {
                    String[] dateTimeParts = dateString.split(" ");
                    if (dateTimeParts.length >= 1) {
                        String datePart = dateTimeParts[0]; // 日期部分
                        String[] dateParts = datePart.split("-"); // 年、月、日
                        if (dateParts.length >= 2) {
                            String yearMonth = dateParts[0] + "-" + dateParts[1]; // 年-月
                            int userId = Integer.parseInt(row.get(1)); // 第二列是用户数量
                            // 将用户ID添加到对应月份的Set中，实现去重
                            Set<String> users = monthlyUsers.getOrDefault(yearMonth, new HashSet<>());
                            users.add(userId + "");
                            monthlyUsers.put(yearMonth, users);


                            monthlyTotal.merge(yearMonth, 1, Integer::sum);
                        }
                    }
                }
            }

            reader.close();

            System.out.println("end file:" + name + ", cost:" + timer.intervalPretty());
        }

        // 输出每个月的用户数量统计
        for (Map.Entry<String, Set<String>> entry : monthlyUsers.entrySet()) {
            System.out.println("Month: " + entry.getKey() + ", User Count: " + entry.getValue().size());
        }
        System.out.println("总量:" + total);
        System.out.println("文件总量:" + ls.length);
        for (Map.Entry<String,Integer> entry : monthlyTotal.entrySet()) {
            System.out.println("Month: " + entry.getKey() + ", Total Count: " + entry.getValue());
        }
    }
}
