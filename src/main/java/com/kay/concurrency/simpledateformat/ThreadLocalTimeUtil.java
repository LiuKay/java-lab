package com.kay.concurrency.simpledateformat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kay on 2018/5/28.
 * jdk 1.8
 */
public class ThreadLocalTimeUtil {

    private static final String STANDARD_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    //初始化对象
    private final static ThreadLocal<SimpleDateFormat> sdfHolder = ThreadLocal.withInitial(() -> new SimpleDateFormat(STANDARD_FORMAT_STR));


    public static Date parse(String dateStr) throws ParseException {
        return sdfHolder.get().parse(dateStr);
    }

    public static String format(Date date){
        return sdfHolder.get().format(date);
    }
}
