package com.kay.concurrency.future.example;

/**
 * Created by kay on 2017/9/6.
 */
public class Client {

    public Data handle(String param) {
        final FutureData futureData = new FutureData();

        //启动后台去装配数据
        new Thread() {
            @Override
            public void run() {
                RealData realData = new RealData(param);
                futureData.setRealData(realData);
            }
        }.start();

        return futureData;
    }
}
