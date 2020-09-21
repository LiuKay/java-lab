package com.kay.jvm.lock;

import java.util.concurrent.TimeUnit;
import org.openjdk.jol.info.ClassLayout;

/**
 * -XX:BiasedLockingStartupDelay=0 关闭偏向锁延迟
 */
public class MarkWordTest {

		public static void main(String[] args) throws InterruptedException {
				TimeUnit.SECONDS.sleep(5);
				Object object = new Object();
				Thread thread1 = new Thread() {
						@Override
						public void run() {
								synchronized (object) {
										System.out.println("thread1 获得偏向锁");
										System.out.println(ClassLayout.parseInstance(object).toPrintable());
										try {
												//让线程晚点儿死亡，造成锁的竞争
												TimeUnit.SECONDS.sleep(6);
										} catch (InterruptedException e) {
												e.printStackTrace();
										}
										System.out.println("thread2 获取锁失败导致锁升级,此时thread1还在执行");
										System.out.println(ClassLayout.parseInstance(object).toPrintable());
								}
						}
				};
				Thread thread2 = new Thread() {
						@Override
						public void run() {
								synchronized (object) {
										System.out.println("thread2 获取偏向锁失败，最终升级为重量级锁，等待thread1执行完毕，获取重量锁成功");
										System.out.println(ClassLayout.parseInstance(object).toPrintable());
										try {
												TimeUnit.SECONDS.sleep(3);
										} catch (InterruptedException e) {
												e.printStackTrace();
										}
								}
						}
				};
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
						synchronized (object) {
								System.out.println("再次获取");
								System.out.println(ClassLayout.parseInstance(object).toPrintable());
						}
				});
				t3.start();
		}

}
