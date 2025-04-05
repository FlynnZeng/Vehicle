package com.me.vehicle.utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * 日期格式化工具栏
 */
public class DateUtils {
    public static String convertToCN(String shortDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);

        try {
            Date date = inputFormat.parse(shortDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Objects.requireNonNull(date));


            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String stringToDate(String str) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(str);
            return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String timeToHhMm(String rawTime) {
        Time time = stringToHhMm(rawTime);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return sdf.format(new Date(time.getTime())); // 转换为 HH:mm 格式的字符串
    }

    private static Time stringToHhMm(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.CHINA);
        try {
            long ms = Objects.requireNonNull(sdf.parse(str)).getTime();
            return new Time(ms);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}

