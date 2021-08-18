package com.kay;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ArrayListTests {
    @Test
    void test1() {
        String[] arr = new String[]{"a", "b", "c"};
        final List<String> strings = Arrays.asList(arr);
        arr[0] = "xx";

        assertThat(strings.get(0)).isEqualTo("xx");
    }


    @Test
    void test2() {
        int[] myArray = {1, 2, 3};
        List myList = Arrays.asList(myArray);

        System.out.println(myList.getClass());

        System.out.println(myList.size());//1
        System.out.println(myList.get(0));//数组地址值
        System.out.println(myList.get(1));//报错：ArrayIndexOutOfBoundsException
        int[] array = (int[]) myList.get(0);
        System.out.println(array[0]);//1
    }

    @Test
    void test3() {
        int[] myArray2 = {1, 2, 3};
        List myList = Arrays.stream(myArray2).boxed().collect(Collectors.toList());

        assertThat(myList).hasSize(3);
    }

    @Test
    void test4() {
        String[] s = new String[]{
                "dog", "lazy", "a", "over", "jumps", "fox", "brown", "quick", "A"
        };
        List<String> list = Arrays.asList(s);
        Collections.reverse(list);
        s = list.toArray(new String[0]);//没有指定类型的话会报错
    }

    @Test
    void test5() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");


        for (String s : list) {
            if ("2".equals(s)) {
                list.remove(s); // ConcurrentModificationException
            }
// but if :
//            if ("3".equals(s)) {
//                list.remove(s); // modCount == expectedModCount ??
//            }
        }

    }
}
