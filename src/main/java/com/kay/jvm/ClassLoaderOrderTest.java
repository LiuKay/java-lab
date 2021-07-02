package com.kay.jvm;

public class ClassLoaderOrderTest {

    private int a;

    private static final int count = 0;

    private static final Integer NUM = 1;

    private static final ClassLoaderOrderTest CLASS_LOADER_ORDER_TEST = new ClassLoaderOrderTest();

    static {
        System.out.println("静态代码块执行");
    }

    {
        a = 222;
        System.out.println("成员代码块执行(代码顺序在构造函数前)，赋值a=" + a);
    }

    public ClassLoaderOrderTest() {
        a = 111;
        System.out.println("构造函数执行,赋值 a=" + a);
    }

    {
        a = 333;
        System.out.println("成员代码块执行(代码顺序在构造函数后)，赋值a="+a);
    }


    static final class Test{

        public static void main(String[] args) {
            System.out.println("访问 final 基本变量 count:"+ClassLoaderOrderTest.count);
            System.out.println("--------------------------");
            System.out.println("访问 final 引用类型 NUM:"+ClassLoaderOrderTest.NUM);

            System.out.println("最后 a=" + ClassLoaderOrderTest.CLASS_LOADER_ORDER_TEST.a);
        }
    }


}
