package com.kay.jvm.other;

/**
 * javap Dispatch.class:
 * <p>
 * class com.kay.jvm.other.Dispatch {
 * com.kay.jvm.other.Dispatch();
 * Code:
 * 0: aload_0
 * 1: invokespecial #1                  // Method java/lang/Object."<init>":()V
 * 4: return
 * <p>
 * public static void main(java.lang.String[]);
 * Code:
 * 0: new           #2                  // class com/kay/jvm/other/Dispatch$Father
 * 3: dup
 * 4: invokespecial #3                  // Method com/kay/jvm/other/Dispatch$Father."<init>":()V
 * 7: astore_1
 * 8: new           #4                  // class com/kay/jvm/other/Dispatch$Son
 * 11: dup
 * 12: invokespecial #5                  // Method com/kay/jvm/other/Dispatch$Son."<init>":()V
 * 15: astore_2
 * 16: aload_1
 * 17: new           #6                  // class com/kay/jvm/other/Dispatch$A
 * 20: dup
 * 21: invokespecial #7                  // Method com/kay/jvm/other/Dispatch$A."<init>":()V
 * 24: invokevirtual #8                  // Method com/kay/jvm/other/Dispatch$Father.show:(Lcom/kay/jvm/other/Dispatch$A;)V
 * 27: aload_2
 * 28: new           #9                  // class com/kay/jvm/other/Dispatch$B
 * 31: dup
 * 32: invokespecial #10                 // Method com/kay/jvm/other/Dispatch$B."<init>":()V
 * 35: invokevirtual #11                 // Method com/kay/jvm/other/Dispatch$Father.show:(Lcom/kay/jvm/other/Dispatch$B;)V
 * 38: return
 * }
 */
class Dispatch {

    public static void main(String[] args) {
        Father father = new Father();
        Father son = new Son();
        //method overload
        father.show(new A()); // invokevirtual
        //method override
        son.show(new B()); //invokevirtual
    }

    static class Father {
        public void show(A a) {
            System.out.println("父类showA");
        }

        //method overload show(A a)
        public void show(B b) {
            System.out.println("父类showB");
        }
    }

    static class Son extends Father {
        @Override
        public void show(A a) {
            System.out.println("子类showA");
        }

        @Override
        public void show(B b) {
            System.out.println("子类showB");
        }
    }

    private static class A {
    }

    private static class B {
    }

}
