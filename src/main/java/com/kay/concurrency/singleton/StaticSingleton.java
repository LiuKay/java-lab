package com.kay.concurrency.singleton;

/**
 * Created by kay on 2017/9/5.
 * 使用内部类实现的单例模式
 * 优点：1.使用单例的时候才会创建实例
 *      2.调用但单例类的其他静态成员不会创建实例
 *      比如 如果声明为
 *      private  static StaticSingleton instance=new StaticSingleton();
 *      则当调用 StaticSingleton.status时，该对象也会创建，而放在内部类中就不会有这个问题了
 */
public class StaticSingleton {

    public static int status=0;

    public StaticSingleton(){
        System.out.println(" init..");
    }

    private static class StaticSingleHolder{
        private static StaticSingleton instance=new StaticSingleton();
    }

    public static StaticSingleton getInstance(){
        return StaticSingleHolder.instance;
    }

    public static void main(String[] args) {
        StaticSingleton s=getInstance();
//        System.out.println(StaticSingleton.status);
    }

}

