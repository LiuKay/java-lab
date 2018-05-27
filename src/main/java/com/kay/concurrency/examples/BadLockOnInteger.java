package com.kay.concurrency.examples;

/**
 * Created by kay on 2017/9/1.
 * 该例子主要是为了说明对Integer对象的加锁问题，Integer对象时不可变对象（final），
 * 用它作为计数器时，每次加1返回的其实是一个新的Integer对象，也就是counter不是同一个对象了
 * 故在counter上加的锁没有起到作用
 * 解决：synchronized(instance)或synchronized (BadLockOnInteger.class)
 */
public class BadLockOnInteger implements Runnable{
    public static Integer counter=0;
    static BadLockOnInteger instance=new BadLockOnInteger();

    @Override
    public void run() {
        for (int j=0;j<10000;j++) {
            synchronized (counter){
                counter++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(counter);
    }
}
