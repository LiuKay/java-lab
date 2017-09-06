package com.kay.concurrent.design;

/**
 * Created by kay on 2017/9/5.
 * 缺点：调用Singleton.status时 也会初始化 instance，因为是静态变量
 * 只要访问 Singleton的任何方法或属性都会初始化这些静态变量或成员
 */
public class Singleton {

    public static int status=1;

    private Singleton(){
        System.out.println(" init.......");
    }

    private  static Singleton instance=new Singleton();

    public static Singleton getInstance(){
        return instance;
    }
}
