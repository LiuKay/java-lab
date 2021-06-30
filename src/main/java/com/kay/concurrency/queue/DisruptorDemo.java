package com.kay.concurrency.queue;

import com.kay.concurrency.utils.NamedThreadFactory;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.log4j.Log4j2;

import java.nio.ByteBuffer;

/**
 * Created on 2020/5/17
 *
 * @author: LiuKay
 */
@Log4j2
public class DisruptorDemo {

    public static void main(String[] args) throws InterruptedException {

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Construct the Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize,
                NamedThreadFactory.namedThreadFactory("Disruptor"));

        // Connect the handler
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> log.info("Event: " + event));

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            bb.putLong(0, l);
            ringBuffer.publishEvent((event, sequence, buffer) -> event.set(buffer.getLong(0)), bb);
            Thread.sleep(1000);
        }
    }

    static class LongEvent {

        private long value;

        public void set(long value) {
            this.value = value;
        }
    }

    static class LongEventFactory implements EventFactory<LongEvent> {

        public LongEvent newInstance() {
            return new LongEvent();
        }
    }

    static class LongEventHandler implements EventHandler<LongEvent> {

        public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
            System.out.println("Event: " + event);
        }
    }

}
