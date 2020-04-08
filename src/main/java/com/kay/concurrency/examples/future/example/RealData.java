package com.kay.concurrency.examples.future.example;

/**
 * Created by kay on 2017/9/6.
 */
public class RealData implements Data{

    protected final String result;

    /**
     * 构造真实数据
     * @param param
     */
    public RealData(String param) {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<10;i++) {
            sb.append(param);
            try {

                //模拟耗时操作
                System.out.println(Thread.currentThread().getName()+"-异步获取数据..");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.result=sb.toString();
    }

    @Override
    public String getResult() {
        return result;
    }
}
