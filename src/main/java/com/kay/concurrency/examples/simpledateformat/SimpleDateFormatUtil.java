package com.kay.concurrency.examples.simpledateformat;

import com.kay.concurrency.annotations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by kay on 2018/5/28.
 *
 *  JDK API文档：Date formats are not synchronized. It is recommended to create separate format instances for each thread.
 *   If multiple threads access a format concurrently, it must be synchronized externally.
 *
 */
@Slf4j
@NotThreadSafe
public class SimpleDateFormatUtil {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    public static  String formatDate(Date date)throws ParseException{
        return simpleDateFormat.format(date);
    }

    public static Date parse(String strDate) throws ParseException {

        return simpleDateFormat.parse(strDate);
    }

}
