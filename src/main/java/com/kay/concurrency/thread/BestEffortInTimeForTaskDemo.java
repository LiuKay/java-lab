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
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class BestEffortInTimeForTaskDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final ExecutorService pool = Executors.newFixedThreadPool(10, NamedThreadFactory.namedThreadFactory(
                "work-task"));

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        final Future<?> future = pool.submit(() -> {
            final WorkTask workTask = new WorkTask(2);
            TimeCheckTask timeCheckTask = new TimeCheckTask(workTask);
            final ScheduledFuture<?> schedule = scheduledExecutorService.schedule(timeCheckTask, 10, TimeUnit.SECONDS);
            workTask.process();

            if (!workTask.isTimeout()) {
                //end before timeout
                log.info("task end before timeout.");
                schedule.cancel(false);
            }
        });

        future.get();

        pool.shutdown();
        scheduledExecutorService.shutdown();
        log.info("End.");
    }

    static class WorkTask {
        private volatile boolean timeout = false;
        private final Random random = new Random();

        private final String id;
        private final int num;
        private AtomicInteger stopAt = new AtomicInteger(0);

        public WorkTask(int num) {
            this.num = num;
            this.id = UUID.randomUUID().toString();
        }

        void process() {
            while (!timeout && stopAt.get() <= num) { // normal case: stopAt = num+1
                final int nextInt = random.nextInt(5);
                try {
                    //mock some time cost operation, maybe a blocking operation,an interruptable invoke.
                    TimeUnit.SECONDS.sleep(nextInt);
                    log.info("ended task[" + stopAt + "].(cost {}s)", nextInt);
                    stopAt.getAndIncrement();
                } catch (InterruptedException e) {
                    //response to interruptable invoke
                    Thread.currentThread().interrupt();
                    log.warn("got interrupted at task[{}]", stopAt);
                    break;
                }
            }

            if (timeout) {
                actionAfterTimeout();
            }
        }

        void actionAfterTimeout(){
            log.info("task stop at task[{}](NOT STARTED) due to timeout.", getStopAt());
            //some logic
            log.info("do some last job after timeout.");
        }

        void timeout() {
            Thread.currentThread().interrupt();
            this.timeout = true;
        }

        boolean isFinished() {
            return stopAt.intValue() > num;
        }

        boolean isTimeout() {
            return this.timeout;
        }

        public String getId() {
            return id;
        }

        public int getStopAt() {
            return stopAt.intValue();
        }
    }

    static class TimeCheckTask implements Runnable{
        WorkTask workTask;

        public TimeCheckTask(WorkTask workTask) {
            this.workTask = workTask;
        }

        @Override
        public void run() {
            log.info("Time's up for WorkTask({})", workTask.getId());
            if (workTask.isFinished()) {
                log.info("WorkTask finished before timeout.");
                return;
            }
            workTask.timeout();
        }
    }

}
