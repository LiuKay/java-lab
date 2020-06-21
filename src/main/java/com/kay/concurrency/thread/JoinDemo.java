package com.kay.concurrency.thread;

import com.kay.concurrency.utils.Utils;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;

/**
 * Created by LiuKay on 2020/5/24.
 */
@Log4j2
public class JoinDemo {

    public static void main(String[] args) throws InterruptedException {

        log.info(Thread.currentThread().getName() + " 正在执行");
        Thread t1=new Thread(()->{
            log.info("aaa");
            Utils.sleep(2, TimeUnit.SECONDS);
            log.info(Thread.currentThread().getName() + " 正在执行");
        },"t1");

        Thread t2=new Thread(()->{
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.info("bbb");
            Utils.sleep(2, TimeUnit.SECONDS);
            log.info(Thread.currentThread().getName() + " 正在执行");
        },"t2");

        t1.start();
        t2.start();
        t2.join();

        log.info(Thread.currentThread().getName() + " 正在执行");
    }

}
