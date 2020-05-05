package com.kay.concurrency.design;

import com.kay.concurrency.utils.NamedThreadFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * Created on 5/5/2020
 *
 * @author: LiuKay
 */
@Log4j2
public class ProducerConsumerDemo {

    public static void main(String[] args) {
        BatchService batchService=new BatchService();
        batchService.testBatchExecute();
    }

    /**
     * Case 1 : batch execute tasks
     */
    static class BatchService{
        private static BlockingQueue<Task> queue = new LinkedBlockingQueue(2000);

        public void testBatchExecute() {
            ExecutorService service = Executors
                .newFixedThreadPool(5, NamedThreadFactory.namedThreadFactory("BatchExecutor"));

            for (int i = 0; i < 5; i++) {
                service.execute(()->{
                    try {
                        while (true) {
                            List<Task> tasks = pollTasks();
                            batchExecuteTasks(tasks);
                        }
                    } catch (Exception e) {
                        log.error(e);
                    }
                });
            }

            ScheduledExecutorService producer = Executors
                .newScheduledThreadPool(1, NamedThreadFactory.namedThreadFactory("Producer"));

            producer.scheduleWithFixedDelay(() -> {
                for (int i = 0; i < 5; i++) {
                    queue.add(new Task(UUID.randomUUID(),"Task"));
                }
            }, 5, 5, TimeUnit.SECONDS);
        }

        private void batchExecuteTasks(List<Task> tasks) {
            log.info("Batch execute tasks number={}", tasks.size());
        }

        private List<Task> pollTasks() throws InterruptedException {
            List<Task> tasks = new LinkedList<>();

            Task task = queue.take(); //retrieve the first task,waiting on empty
            while (task != null) {
                tasks.add(task);
                task = queue.poll(); //retrieve other tasks non-blocking
            }
            return tasks;
        }

        @Data
        @AllArgsConstructor
        static class Task{
            private UUID id;
            private String content;
        }
    }

}
