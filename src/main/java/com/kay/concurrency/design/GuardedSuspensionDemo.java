package com.kay.concurrency.design;

import static com.kay.concurrency.utils.Utils.sleep;

import com.kay.concurrency.utils.NamedThreadFactory;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

/**
 * How to turn async to sync 如何异步转同步案例
 */
@Log4j2
public class GuardedSuspensionDemo {

		public static void main(String[] args) {
				GuardedSuspensionDemo demo = new GuardedSuspensionDemo();
				demo.mockService();
		}

		// Mock MQ
		private static final BlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);

		private final ExecutorService consumerService = new ThreadPoolExecutor(1, 1,
				0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>(), new NamedThreadFactory("Consumer"));

		private final ExecutorService callBackService = new ThreadPoolExecutor(1, 1,
				0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>(), new NamedThreadFactory("Callback"));

		void mockService() {
				Message message = new Message(1L, "hello");
				sendMessage(message);

				GuardedObject guardedObject = GuardedObject.create(message.getId());
				// wait for result
				Message re = (Message) guardedObject.get(Objects::nonNull, 2, TimeUnit.SECONDS);

				log.info("Received callback:{}", re);

				callBackService.shutdown();
				consumerService.shutdown();
		}

		void sendMessage(Message message) {
				log.info("Provider send a message to MQ.");
				queue.offer(message);

				//mock callback
				mockAsyncCallback(message.getId());
		}

		//Another thread will call this as callback
		void mockAsyncCallback(Object key) {
				callBackService.execute(() -> {
						Message result = null;
						try {
								result = mockConsumer();
								log.info("Callback is invoked.");
						} finally {
								/**
								 *  TODO: 触发结果回写，唤醒等待线程，如果该方法始终无法得到调用，将导致等待线程超时释放，
								 *  而对于 GuardedObject持有的 map 对应的 (key,value)不会移除，map将产生内存泄漏，
								 *  所有不管是正常返回也好，还是消费异常情况，都要调用此方法
								 */
								GuardedObject.fireEvent(key, result);
						}
				});
		}

		/**
		 * Mock consume message
		 */
		Message mockConsumer() {
				FutureTask<Message> futureTask = new FutureTask<>(() -> {
						Message result = null;
						try {
								Message message = queue.take();
								log.info("Consumer received message from MQ.");

								// consume logic...do sth..
								log.info("Consume message :" + message);
								sleep(2, TimeUnit.SECONDS);

								long key = message.getId();
								result = new Message(key, "call back result");
						} catch (InterruptedException e) {
								e.printStackTrace();
						}
						return result;
				});

				consumerService.submit(futureTask);

				Message r = null;
				try {
						r = futureTask.get();
				} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
				}
				return r;
		}

		@Data
		@ToString
		@AllArgsConstructor
		static class Message {

				private long id;
				private String payload;
		}

		static class GuardedObject<T> {

				private T obj;

				private final Lock lock = new ReentrantLock();

				private final Condition done = lock.newCondition();

				private static Map<Object, GuardedObject> guardedObjectMap = new ConcurrentHashMap<>();

				static GuardedObject create(Object key) {
						GuardedObject guardedObject = new GuardedObject();
						guardedObjectMap.put(key, guardedObject);
						return guardedObject;
				}

				static <K, T> void fireEvent(K key, T obj) {
						GuardedObject guardedObject = guardedObjectMap.remove(key);
						if (guardedObject != null) {
								guardedObject.onChange(obj);
						}
				}

				T get(Predicate<T> p, int timeout, TimeUnit timeUnit) {
						lock.lock();
						try {
								while (!p.test(obj)) {
										log.info(">>AWAIT FOR RESULT");
										done.await(timeout, timeUnit);
								}
						} catch (InterruptedException e) {
								e.printStackTrace();
						} finally {
								lock.unlock();
						}
						log.info("Wake up and return");
						return obj;
				}

				private void onChange(T obj) {
						lock.lock();
						try {
								this.obj = obj;
								log.info(">>onChange is called. Signal waiting threads.");
								done.signalAll();
						} finally {
								lock.unlock();
						}
				}

		}
}
