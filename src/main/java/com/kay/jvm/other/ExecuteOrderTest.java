package com.kay.jvm.other;

public class ExecuteOrderTest {

    public static void main(String[] args) {
        Son son = new Son();
        son.say();
        System.out.println("Done");
    }

    private static class Father {
        static String word = "father static word";
        private final String instanceWord = "father instance word";

        static {
            System.out.println(word);
            System.out.println("father static code");
        }

        {
            System.out.println("father instance code");
        }

        public Father() {
            System.out.println("father constructor code");
        }

        void say() {
            System.out.println(instanceWord);
        }
    }

    private static class Son extends Father {
        static String word = "son static word";
        private final String instanceWord = "son instance word";


        static {
            System.out.println(word);
            System.out.println("son static code");
        }

        {
            System.out.println("son instance code");
        }

        public Son() {
            System.out.println("son constructor code");
        }

        void say() {
            System.out.println(instanceWord);
        }
    }


}
