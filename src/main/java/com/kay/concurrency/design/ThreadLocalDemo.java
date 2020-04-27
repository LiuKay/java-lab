package com.kay.concurrency.design;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kay on 2017/9/5.
 * SimpleDateFormat 不是线程安全的
 * 这里采用ThreadLocal来改造
 */
public class ThreadLocalDemo {


    //public static  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static  ThreadLocal<SimpleDateFormat> simpleDateFormat = new ThreadLocal<>();

    public static class DateParse implements Runnable{

        private int i;

        public DateParse(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            try {
                if (simpleDateFormat.get()==null) {
                    simpleDateFormat.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                }
                SimpleDateFormat format=simpleDateFormat.get();
                Date d=format.parse("2017-9-5 11:07:"+i%60);
                System.out.println(d);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(10);
        for(int i=0;i<60;i++) {
            pool.execute(new DateParse(i));
        }
        pool.shutdown();
    }
}
