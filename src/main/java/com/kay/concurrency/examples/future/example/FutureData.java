package com.kay.concurrency.examples.future.example;

/**
 * Created by kay on 2017/9/6.
 */
public class FutureData implements Data{

    protected RealData realData;

    protected boolean isReady=false;

    @Override
    public synchronized String getResult() {
        //如果没有装配好久等待
        while (!isReady){
            try {
                wait();   //没有准备好就一直等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //装配好了就返回真实的数据
        return realData.result;
    }

    /**
     * 装配 realData 真实的数据，但是realData的构造比较慢，
     * @param realData
     */
    public synchronized void setRealData(RealData realData) {
        if (isReady) {
            return;
        }

        this.realData = realData;
        isReady=true;
        notifyAll(); //唤醒获取
    }


}
