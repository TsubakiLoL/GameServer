package org.tsubaki.Tool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
public class TimeTool {
    // 正确定义的紧凑格式化器
    private static final DateTimeFormatter COMPACT_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static LocalDateTime getNowTime(){
        return LocalDateTime.now();
    }

    public static String getNowTimeString(){
        return toDbString(getNowTime());
    }
    public static long getSecondsDiff(LocalDateTime from,LocalDateTime to){
        // 直接计算秒数差值
        long secondsDiff = ChronoUnit.SECONDS.between(from, to);
        return secondsDiff;
    }
    // 2. 存储到数据库
    public static String toDbString(LocalDateTime ldt) {
        return ldt.format(COMPACT_FORMATTER);
    }

    // 3. 从数据库读取
    public static LocalDateTime fromDbString(String dbString) {
        try {
            return LocalDateTime.parse(dbString, COMPACT_FORMATTER);
        }catch (Exception e){
            return null;
        }
    }


}