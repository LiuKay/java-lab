package com.kay.concurrent.future.jdk_future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by kay on 2017/9/6.
 */
public class FutureMain {
    public static void main(String[] args) throws Exception {
        FutureTask<String> futureTask = new FutureTask<String>(new MyTask("kay"));
        ExecutorService service= Executors.newCachedThreadPool();
        service.submit(futureTask);

        System.out.println("请求已返回");
        System.out.println("执行其他操作");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("获取处理结果："+futureTask.get());
    }
}
