package com.kay.concurrency.synchronizer;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * For better understand "Monitor"
 *
 * @param <T>
 */
public class MyBlockingQueue<T> {

  private Queue<T> queue;

  private ReentrantLock lock = new ReentrantLock();

  private Condition notEmpty = lock.newCondition();

  private Condition notFull = lock.newCondition();

  private volatile int capacity;

  public MyBlockingQueue(int capacity) {
    this.capacity = capacity;
    queue = new ArrayDeque<>();
  }

  public void add(T t) {
    lock.lock();
    try {
      while (capacity == queue.size()) { // condition not match, then await
        System.out.println(Thread.currentThread().getName()
            + ": queue is full, wait on notFull condition...");
        notFull.await();
      }
      queue.add(t);
      //add success, signal waiting threads
      System.out.println(Thread.currentThread().getName() + ": Add queue success.");
      notEmpty.signalAll();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }

  public T pop() {
    T t = null;
    try {
      lock.lock();
      while (queue.size() == 0) { // condition not match, then await
        System.out.println(Thread.currentThread().getName()
            + ": queue is empty. wait on notEmpty condition...");
        notEmpty.await();
      }
      t = queue.poll();

      System.out.println(Thread.currentThread().getName() + ": Poll queue success.");
      notFull.signalAll();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
    return t;
  }


  public static void main(String[] args) throws InterruptedException {
    MyBlockingQueue<String> myQueue = new MyBlockingQueue<>(3);

    Thread taken1 = new Thread(() -> {
      while (true) {
        sleep();
        String pop = myQueue.pop();
        System.out.println(pop);
      }
    }, "TAKEN1");

    Thread offer1 = new Thread(() -> {
      while (true) {
        myQueue.add("Hello");
      }
    }, "OFFER1");

    offer1.start();
    Thread.sleep(1000);
    taken1.start();
  }

  static void sleep() {
    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
