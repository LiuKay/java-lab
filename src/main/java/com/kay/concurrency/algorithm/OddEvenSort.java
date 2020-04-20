package com.kay.concurrency.algorithm;

/**
 * Created by kay on 2017/9/6.
 * 奇偶交换排序 实现的 冒泡排序
 */
public class OddEvenSort {

    public static void oddEvenSort(int arr[]) {
        boolean exFlag=true;
        int start=0;
        while (exFlag || start == 0) {
            exFlag=false;
            for (int i=start;i<arr.length-1;i+=2) {
                if (arr[i]>arr[i+1]) {
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    exFlag=true;
                }
            }
            if (start == 0) {
                start = 1;
            } else {
                start=0;
            }
        }
    }


    public static void bubbleSort(int[] arr) {
        boolean exFlag=true;
        for (int i=arr.length-1;i>=0 && exFlag;i--) {
            exFlag=false;
            for (int j=0;j<i;j++) {
                if (arr[j]>arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    exFlag=true;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] arr={9,57,35,45,2,41,6,5,22,33,44,85,20,32,13,17,29,49};
        //oddEvenSort(arr);
        bubbleSort(arr);
        for (int i=0;i<arr.length;i++) {
            System.out.print(arr[i]+",");
        }

    }
}
