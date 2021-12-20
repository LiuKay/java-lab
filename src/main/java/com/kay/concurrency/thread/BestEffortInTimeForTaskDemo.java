package com.kay.concurrency.thread;


import com.kay.concurrency.utils.NamedThreadFactory;
import lombok.extern.log4j.Log4j2;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Log4j2
public class BestEffortInTimeForTaskDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final ExecutorService pool = Executors.newFixedThreadPool(10, NamedThreadFactory.namedThreadFactory(
                "work-task"));

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        final Future<?> future = pool.submit(() -> {
            final WorkTask workTask = new WorkTask(10);
            TimeCheckTask timeCheckTask = new TimeCheckTask(workTask);
            final ScheduledFuture<?> schedule = scheduledExecutorService.schedule(timeCheckTask, 10, TimeUnit.SECONDS);
            workTask.process();

            if (!workTask.isTimeOver()) {
                //end before timeout
                log.info("task end before timeout.");
                schedule.cancel(false);
            }

        });

        future.get();

        log.info("End.");
    }

    static class WorkTask {
        private String id;

        private volatile boolean timeOver = false;
        private volatile boolean finish = false;

        private Random random = new Random();
        private int num;

        public WorkTask(int num) {
            this.num = num;
            this.id = UUID.randomUUID().toString();
        }

        void process() {
            for (int i = 0; i < this.num; i++) {
                if (!timeOver) {
                    final int nextInt = random.nextInt(5);
                    try {
                        TimeUnit.SECONDS.sleep(nextInt);
                    } catch (InterruptedException e) {
                        log.warn("interrupt");
                        Thread.currentThread().interrupt();
                    }
                    log.info("ended task[" + i + "].(cost {}s)",nextInt);
                } else {
                    log.info("time exceed. stop at task[" + i + "]. (not started)");
                    break;
                }
            }
            this.finish = true;

            if (timeOver) {
                actionAfterTimeout();
            }
        }

        void actionAfterTimeout(){
            log.info("do some last job after timeout.");
        }

        void timeout() {
            this.timeOver = true;
        }

        boolean isFinished() {
            return finish;
        }

        boolean isTimeOver() {
            return this.timeOver;
        }

        public String getId() {
            return id;
        }
    }

    static class TimeCheckTask implements Runnable{
        WorkTask workTask;

        public TimeCheckTask(WorkTask workTask) {
            this.workTask = workTask;
        }

        @Override
        public void run() {
            log.info("time's up for task({})", workTask.getId());
            if (workTask.isFinished()) {
                log.info("task finished before timeout.");
                return;
            }
            workTask.timeout();
            log.info("task stopped by timeout.");
        }
    }

}
