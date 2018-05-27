package com.kay.concurrency.examples.tracestack;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by kay on 2017/9/4.
 *
 * 该例子演示 出现异常状况时，丢失堆栈信息
 * 在例子中 100除以0，1，2，3，4
 * 出现结果只有4个，当除以 0时，程序没有报错，也没有结果，在复杂系统中这类错误时致命的
 */
public class DivTask implements Runnable{

    int a,b;

    public DivTask(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        double r=a/b;
        System.out.println(r);
    }

    public static void main(String[] args) {
        ThreadPoolExecutor pool=new ThreadPoolExecutor(0,Integer.MAX_VALUE,0L,
                 TimeUnit.MILLISECONDS,new SynchronousQueue());

        for (int i=0;i<5;i++) {
            pool.submit(new DivTask(100, i));
        }
    }
}
