package com.kay.jvm.other;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ExecuteOrderTest {

    public static void main(String[] args) {
        Son son = new Son();
        son.say();
        log.info("Done");
    }

    private static class Father {
        static String word = "father static word";
        private final String instanceWord = "father instance word";

        static {
            log.info(word);
            log.info("father static code");
        }

        {
            log.info("father instance code");
        }

        public Father() {
            log.info("father constructor code");
        }

        void say() {
            log.info(instanceWord);
        }
    }

    private static class Son extends Father {
        static String word = "son static word";
        private final String instanceWord = "son instance word";


        static {
            log.info(word);
            log.info("son static code");
        }

        {
            log.info("son instance code");
        }

        public Son() {
            log.info("son constructor code");
        }

        void say() {
            log.info(instanceWord);
        }
    }


}
