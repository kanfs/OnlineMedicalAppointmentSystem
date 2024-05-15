package com.kanfs.omas.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateWeekUtil {

    /**
     * 获取周几
     *
     * @param localDate 日期，格式为：2022-08-01
     * @return 周几
     * @throws java.text.ParseException 异常
     */
    public static String dateToWeek(LocalDate localDate) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        // 获得一个日历
        Calendar cal = Calendar.getInstance();
        cal.setTime(
                Date.from(
                        localDate.atStartOfDay()
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                )
        );
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }
}
