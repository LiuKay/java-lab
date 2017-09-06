package com.kay.concurrent.threadlocal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kay on 2017/9/5.
 */
public class ThreadLocalGCDemo {

    public static  volatile ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected void finalize() throws Throwable {
            System.out.println("info ：GC 收回 threadLocal 对象");
        }
    };

    public static volatile CountDownLatch countDown = new CountDownLatch(10000);

    public static class DateParse implements Runnable{

        private int i;

        public DateParse(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            try {
                if (threadLocal.get()==null) {
                    threadLocal.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"){
                        @Override
                        protected void finalize() throws Throwable {
                            System.out.println("info : GC 收回 SimpleDateFormat 对象");
                        }
                    });
                    System.out.println(Thread.currentThread().getName()+" :创建 SimpleDateFormat 对象 ");
                }
                SimpleDateFormat format=threadLocal.get();
                Date d=format.parse("2017-9-5 11:07:"+i%60);
            } catch (ParseException e) {
                e.printStackTrace();
            }finally {
                countDown.countDown();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        ExecutorService pool = Executors.newFixedThreadPool(10);
        for(int i=0;i<10000;i++) {
            pool.execute(new ThreadLocalGCDemo.DateParse(i));
        }
        countDown.await();
        System.out.println("info : 执行完成，开始GC");
        threadLocal=null;
        System.gc();
        System.out.println("第一次GC完成");

        //threadLocal = new ThreadLocal<SimpleDateFormat>();
        //countDown = new CountDownLatch(10000);
        //for(int i=0;i<10000;i++) {
        //    pool.execute(new ThreadLocalGCDemo.DateParse(i));
        //}
        //countDown.await();
        //Thread.sleep(2000);
        //threadLocal=null;
        //System.gc();
        //System.out.println("第二次GC完成");

    }
}
