package com.kay.concurrency.examples.algorithm;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by kay on 2018/6/1.
 */
public class SortedSet {


    public static int[] sortAndDeduplication(int[] arr){
        int len = arr.length;
        for (int i=0;i<len;i++) {
            int min = i;
            for (int j=i+1;j<len;j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
                int temp = arr[j];
                arr[j] = arr[min];
                arr[min] = temp;
            }
        }
        return arr;
    }

    public static void main(String[] args) {
        Integer[] arr={10,10,10,2,3,5,1,6,2,3};
        TreeSet set = new TreeSet();
        set.addAll(Arrays.asList(arr));
        System.out.println(Arrays.toString(set.toArray(new Integer[]{})));
    }
}
