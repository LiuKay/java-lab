package com.kay.jvm;

public class ConditionalOperator {

    public static void main(String[] args) {
        Integer a = 1;
        Integer b = 2;
        Integer c = null;

//        Integer d = false ? a + b : c; // NPE
        Integer d = true ? a + b : c; // non-NPE
    }

}
