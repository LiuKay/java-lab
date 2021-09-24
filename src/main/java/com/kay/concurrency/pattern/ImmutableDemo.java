package com.kay.concurrency.pattern;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Immutability 模式需要注意的2个问题： 1. 不变的边界在哪里,是否要求属性也不可修改，对象的所有属性都是 final 的，并不能保证不可变性.
 * 例如属性是一个集合类，集合的引用无法修改，但是其中的元素却是可以改变的。 2. 正确的发布对象
 */
public class ImmutableDemo {

    public static void main(String[] args) {
        //jdk provide
        Map<String, String> jdkMap = Collections.unmodifiableMap(new HashMap<>());

        //guava
        ImmutableList<String> immutableList = ImmutableList.of("a", "b", "c");
        ImmutableMap<String, String> immutableMap = ImmutableMap.<String, String>builder()
                .put("k1", "v1").build();
//        immutableList.add("d"); //Deprecated
//        jdkMap.put("a", "b"); //不提示

        //自己实现的
        MyImmutableArrayList<String> list = MyImmutableArrayList.of("A", "B", "C");
//        list.add("D"); // UnsupportedOperationException
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
//            iterator.remove(); // UnsupportedOperationException
        }
    }

    //不可变对象
    private static final class Foo {

        private final int age;

        public Foo(int age) {
            this.age = age;
        }

        // 利用享元模式来缓存经常使用的对象，减少对象创建开销，类似于 Integer,Long 里面的缓存
        public static Foo valueOf(int age) {
            if (age < 0) {
                throw new IllegalArgumentException();
            }
            if (age < 100) {
                return FooCache.cache[age];
            }
            return new Foo(age);
        }

        public final int getAge() {
            return this.age;
        }

        //修改时返回新的不可变对象
        public Foo setAge(int age) {
            return new Foo(age);
        }

        private static class FooCache {

            static final Foo[] cache = new Foo[100];

            static {
                for (int i = 0; i < cache.length; i++) {
                    cache[i] = new Foo(i);
                }
            }

            private FooCache() {
            }
        }
    }

    //线程不安全对象
    private class Bar {

        private Foo foo;

        //虽然 Foo类本身是 final的，但是在多线程并发下，此处无法保证对 foo修改的原子性和可见性
        void setFoo(Foo f) {
            this.foo = f;
        }
    }

    private class SafeBar {

        private final AtomicReference<Foo> atomicReference = new AtomicReference<>(new Foo(0));

        void setFoo(Foo foo) {
            while (true) {
                Foo foo1 = atomicReference.get();
                if (atomicReference.compareAndSet(foo1, foo)) {
                    return;
                }
            }
        }
    }

}
