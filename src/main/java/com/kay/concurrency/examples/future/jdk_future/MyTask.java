package com.kay.concurrency.examples.future.jdk_future;

import java.util.concurrent.Callable;

/**
 * Created by kay on 2017/9/6.
 */
public class MyTask implements Callable{

    private String param;

    public MyTask(String param) {
        this.param = param;
    }

    @Override
    public String call() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<10;i++) {
            sb.append(param);
            try {
                //模拟耗时操作
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
