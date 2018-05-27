package com.kay.concurrency.examples.future;

/**
 * Created by kay on 2017/9/6.
 * Future 模式
 * 客户端Cilent调用立即返回一个结果，异步去处理真实的请求
 * 主线程可以继续做其他的事情，等待数据真正返回就可以直接使用了
 *
 */
public class Main {
    public static void main(String[] args) {
        Client client = new Client();
        Data data=client.handle("request");
        //System.out.println("获取真实数据："+ data.);
        System.out.println("请求完成，已经返回");

        System.out.println(Thread.currentThread().getName()+"-做其他的事");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("获取真实数据："+ data.getResult());
    }
}
