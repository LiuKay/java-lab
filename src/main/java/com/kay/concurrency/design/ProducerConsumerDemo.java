package com.kay.concurrency.design;

import com.kay.concurrency.utils.NamedThreadFactory;
import com.kay.concurrency.utils.TestUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

/**
 * Created on 5/5/2020
 *
 * @author: LiuKay
 */
@Log4j2
public class ProducerConsumerDemo {

    public static void main(String[] args) {
//        BatchService batchService=new BatchService();
//        batchService.testBatchExecute();

        SectionCommit sectionCommit = new SectionCommit();
        sectionCommit.testLogService();
    }

    /**
     * Case 1 : batch execute tasks
     */
    static class BatchService {

        private static BlockingQueue<Task> queue = new LinkedBlockingQueue<>(1000);

        public void testBatchExecute() {
            ExecutorService service = Executors
                .newFixedThreadPool(5, NamedThreadFactory.namedThreadFactory("BatchExecutor"));

            for (int i = 0; i < 5; i++) {
                service.execute(() -> {
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
        static class Task {

            private UUID id;
            private String content;
        }
    }

    /**
     * Case 2: 分段提交 案例：日志刷盘 规则： 1.ERROR 立即刷盘 2. 累积 500条刷盘 3. 5秒内有未刷盘日志立即刷盘
     */
    static class SectionCommit {

        private final LogMsg POISON_MSG = new LogMsg(LEVEL.ERROR, "POISON-LogMsg");

        BlockingQueue<LogMsg> queue = new LinkedBlockingQueue<>(2000);
        ExecutorService es = Executors
            .newFixedThreadPool(1, NamedThreadFactory.namedThreadFactory("LOG-flush"));

        void testLogService() {
            try {
                flush();
            } catch (IOException e) {
                log.error(e);
            }

            ScheduledExecutorService errorService = Executors
                .newSingleThreadScheduledExecutor(NamedThreadFactory.namedThreadFactory("Log-e"));

            errorService.scheduleWithFixedDelay(() -> error("e"), 10, 10, TimeUnit.SECONDS);

            ScheduledExecutorService infoService = Executors
                .newSingleThreadScheduledExecutor(NamedThreadFactory.namedThreadFactory("Log-i"));

            infoService.scheduleWithFixedDelay(() -> info("info msg"), 1, 1, TimeUnit.SECONDS);

            TestUtils.sleep(15, TimeUnit.SECONDS);
            infoService.shutdown();
            errorService.shutdown();
            stop();
        }

        void flush() throws IOException {
            File file = File.createTempFile("test", ".log");
            FileWriter writer = new FileWriter(file);
            es.execute(() -> {
                try {
                    int sumMsg = 0;
                    long start = System.currentTimeMillis();
                    while (true) {
                        LogMsg msg = queue.poll(5, TimeUnit.SECONDS);

                        if (msg != null) {
                            if (POISON_MSG.equals(msg)) {
                                break;
                            }
                            writer.write(msg.toString());
                            sumMsg++;
                        }

                        long current = System.currentTimeMillis();
                        // flush condition
                        if (msg != null && (msg.level == LEVEL.ERROR || sumMsg >= 500
                            || (current - start) >= 5000)) {
                            log.info("flush logs.");
                            writer.flush();
                            sumMsg = 0;
                            start = System.currentTimeMillis();
                        }

                    }
                } catch (Exception e) {
                    log.error(e);
                } finally {
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        log.error(e);
                    }
                }
            });
            es.shutdown();
        }

        enum LEVEL {
            INFO, ERROR
        }

        @Data
        @AllArgsConstructor
        @EqualsAndHashCode
        class LogMsg {

            private LEVEL level;
            private String msg;

            @Override
            public String toString() {
                return "LogMsg{level=" + level + ", msg=" + msg + "}\n";
            }
        }

        void info(String msg) {
            log.info("add info msg");
            queue.add(new LogMsg(LEVEL.INFO, msg));
        }

        void error(String msg) {
            log.info("add error msg");
            queue.add(new LogMsg(LEVEL.ERROR, msg));
        }

        // poison is used to stop the log thread
        private void stop() {
            queue.add(POISON_MSG);
        }
    }
}
