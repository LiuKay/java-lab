package com.kay.concurrency.examples.simpledateformat;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by kay on 2018/5/28.
 * Joda-time api 实现日期转换
 */
public class DateTimeUtil {
    private static final String STANDARD_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    public static String dateToStr(Date date,String formatStr){
        if (date == null) {
            return "";
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    public static Date strToDate(String dateStr, String formatStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateStr);
        return dateTime.toDate();
    }


    public static String dateToStr(Date date){
        return dateToStr(date, STANDARD_FORMAT_STR);
    }

    public static Date strToDate(String dateStr) {
        return strToDate(dateStr, STANDARD_FORMAT_STR);
    }
}
