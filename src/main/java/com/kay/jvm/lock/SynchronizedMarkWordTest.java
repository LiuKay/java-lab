package com.kay.jvm.lock;

import lombok.extern.log4j.Log4j2;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * 参考：https://blog.csdn.net/qq_36434742/article/details/106854061
 * <p>
 * -XX:-UseCompressedOops 关闭指针压缩(关闭指针压缩后，引用大小将占用 8bytes，开启后占用4bytes，其他基本类型大小不变)
 * -XX:BiasedLockingStartupDelay=0 关闭偏向锁延迟
 * -XX:+UseBiasedLocking 启用偏向锁
 * <p>
 * 64位JVM，关闭指针压缩（-XX:-UseCompressedOops），对象头占128位，其中 Mark Word 64位，
 * 打印出来的对象头，前 8位的信息为：
 * unused(1)+分代年龄(4)+偏向模式(1)+对象锁状态(2)
 * 对象锁状态有： 01(未锁定/可偏向)，00(轻量级锁)，10(重量级锁)，11(GC标记)，
 */
@Log4j2
public class SynchronizedMarkWordTest {

    private static final Object object = new Object();

    public static void main(String[] args) throws InterruptedException {
//        log.info(VM.current().details());

//        ClassLayoutTest.showClassLayout();
//        ClassLayoutTest.showObjectLayout();

//        SynchronizeTest.testLockMarkWord();
        SynchronizeTest.test();
    }

    private static class ClassLayoutTest {
        static void showClassLayout() {
            log.info(ClassLayout.parseClass(ClassLayoutTest.class).toPrintable());
        }

        static void showObjectLayout() {
            log.info(ClassLayout.parseInstance(object).toPrintable());
            log.info("===set hash code:" + System.identityHashCode(object));
            log.info(ClassLayout.parseInstance(object).toPrintable());
        }
    }

    private static class SynchronizeTest {

        static void testLockMarkWord() throws InterruptedException {
            Thread.sleep(3000);

            Object lock = new Object();
            log.info("before lock");
            log.info(ClassLayout.parseInstance(lock).toPrintable());

            //计算 hash code 之后就不能使用偏向锁了
            log.info("hash code:" + lock.hashCode());

            synchronized (lock) {
                log.info("locking");
                log.info(ClassLayout.parseInstance(lock).toPrintable());
            }

            log.info("after lock");
            log.info(ClassLayout.parseInstance(lock).toPrintable());

        }

        static void test() throws InterruptedException {
            TimeUnit.SECONDS.sleep(5);
            Object lock = new Object();

            log.info("lock initial layout:");
            log.info(ClassLayout.parseInstance(lock).toPrintable());

            Thread thread1 = new Thread(() -> {
                synchronized (lock) {
                    log.info("thread1 获得偏向锁");
                    log.info(ClassLayout.parseInstance(lock).toPrintable());
                    log.info("当前线程ID:" + Thread.currentThread().getId());
                    try {
                        //让线程晚点儿死亡，造成锁的竞争
                        TimeUnit.SECONDS.sleep(6);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("thread2 获取锁失败导致锁升级,此时thread1还在执行");
                    log.info("当前线程：" + Thread.currentThread().getName());
                    log.info(ClassLayout.parseInstance(lock).toPrintable());
                }
            }, "t1");

            Thread thread2 = new Thread(() -> {
                synchronized (lock) {
                    log.info("thread2 获取重量锁成功");
                    log.info(ClassLayout.parseInstance(lock).toPrintable());
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "t2");
            thread1.start();
            //对象头打印需要时间,先让thread1获取偏向锁
            TimeUnit.SECONDS.sleep(5);
            //thread2去获取锁，因为t1一直在占用，导致最终升级为重量级锁
            thread2.start();

            //确保t1和t2执行结束
            thread1.join();
            thread2.join();
            TimeUnit.SECONDS.sleep(1);

            Thread t3 = new Thread(() -> {
                synchronized (lock) {
                    log.info("thread3 去获取锁 => 轻量级锁");
                    log.info(ClassLayout.parseInstance(lock).toPrintable());
                }
            }, "t3");

            t3.start();
        }

    }
}
