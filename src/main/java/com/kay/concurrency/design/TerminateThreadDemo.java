package com.kay.concurrency.design;

import com.kay.concurrency.utils.Utils;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;

/**
 * Created on 5/4/2020
 * 两阶段终止线程
 */
@Log4j2
public class TerminateThreadDemo {

    public static void main(String[] args) {
        Proxy proxy = new Proxy();
        proxy.start();
        Utils.sleep(5, TimeUnit.SECONDS);
        proxy.stop();
    }

    static class Proxy{

        private volatile boolean isTerminated=false;
        private boolean started = false;
        private Thread proxyThread;

        synchronized void start(){
            if (started) {
                return;
            }
            started = true;
            log.info("Start");

            proxyThread = new Thread(() -> {
                while (!isTerminated) {
                    thirdPartyMethod();
                }
//                while (!Thread.currentThread().isInterrupted()) {
//                    //maybe it's a third party method, not sure is it set the interrupted state correctly
//                    thirdPartyMethod();
//                }
                log.info("thread is going to stop.");
                started = false;
            }, "ProxyThread");
            proxyThread.start();
        }

        void stop(){
            log.info("fire stop");
            isTerminated = true;
            proxyThread.interrupt();
        }

        void thirdPartyMethod() {
            log.info("Do some work per 2 seconds");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                //may not set
//                Thread.currentThread().interrupt();
            }
        }
    }

}
