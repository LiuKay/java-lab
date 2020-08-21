package com.kay.concurrency.design;

import static com.kay.concurrency.utils.NamedThreadFactory.namedThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;

/**
 * Balking 模式
 * 以简单的自动保存为案例
 */
@Log4j2
public class BalkingPatternDemo {

    public static void main(String[] args) {
        BalkingPatternDemo demo = new BalkingPatternDemo();
        demo.testAutoSave();
    }

    void testAutoSave() {
        AutoSaveClass saveClass = new AutoSaveClass();
        saveClass.startAutoSave();

        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor(namedThreadFactory("Edit"));

        service.scheduleWithFixedDelay(saveClass::edit, 8, 8, TimeUnit.SECONDS);

    }

    static class AutoSaveClass{
        private volatile boolean changed = false;

        private ScheduledExecutorService schedule = Executors
                .newSingleThreadScheduledExecutor(namedThreadFactory("AutoSave"));

        void startAutoSave() {
            schedule.scheduleWithFixedDelay(this::autoSave, 5, 5, TimeUnit.SECONDS);
        }

        void autoSave() {
            if (!changed) {
                log.info("No changes. Balk at saving.");
                return;
            }
            changed = false;
            log.info("Auto-saving..");
        }

        void edit(){
            log.info("Edit....");
            change();
        }

        void change() {
            log.info("changed!");
            changed = true;
        }
    }

}
