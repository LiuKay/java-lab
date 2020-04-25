package com.kay.concurrency.aqs;

import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock ：
 * Write - exclusive lock
 * Read - non-exclusive lock
 * OptimisticRead - no lock
 * Created by kay on 2018/5/29.
 */
public class StampedLockExample {

    class Point {
        private double x, y;
        private final StampedLock sl = new StampedLock();

        void move(double deltaX, double deltaY) { // an exclusively locked method
            long stamp = sl.writeLock();
            try {
                x += deltaX;
                y += deltaY;
            } finally {
                sl.unlockWrite(stamp);
            }
        }


        double distanceFromOrigin() { // A read-only method
            long stamp = sl.tryOptimisticRead(); // 乐观读，
            double currentX = x, currentY = y;  //将两个字段读入本地局部变量
            if (!sl.validate(stamp)) { //检查发出乐观读锁后同时是否有其他写锁发生？
                stamp = sl.readLock();  // 数据已经被修改，升级为读锁
                try {
                    currentX = x;
                    currentY = y;
                } finally {
                    sl.unlockRead(stamp);
                }
            }
            return Math.sqrt(currentX * currentX + currentY * currentY);
        }

        //upgrade -锁的升级
        void moveIfAtOrigin(double newX, double newY) { // upgrade
            // Could instead start with optimistic, not read mode
            long stamp = sl.readLock();
            try {
                while (x == 0.0 && y == 0.0) { //循环，检查当前状态是否符合
                    long ws = sl.tryConvertToWriteLock(stamp); //将读锁转为写锁
                    if (ws != 0L) { //a valid write stamp, or zero on failure
                        stamp = ws; //如果成功 替换票据
                        x = newX;
                        y = newY;
                        break;
                    } else { //如果不能成功转换为写锁
                        sl.unlockRead(stamp);  //显式释放读锁
                        stamp = sl.writeLock();  //显式直接进行写锁 然后再通过循环再试
                    }
                }
            } finally {
                sl.unlock(stamp); //释放读锁或写锁
            }
        }
    }
}
