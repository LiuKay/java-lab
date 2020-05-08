package com.kay.concurrency.forkjoin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import lombok.extern.log4j.Log4j2;

/**
 * Created on 5/7/2020 Demo for invokeAll()
 *
 * @author: LiuKay
 */
@Log4j2
public class MultiRecursiveTaskDemo {

    public static void main(String[] args) {
        int[] arr = new int[50];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        ForkJoinPool pool = new ForkJoinPool(4, NamedForkJoinFactory.create("invokeAll"), null,
            false);
        CustomRecursiveTask task = new CustomRecursiveTask(arr);
        Integer integer = pool.invoke(task);
        System.out.println(integer);
    }

    static class CustomRecursiveTask extends RecursiveTask<Integer> {

        private int[] arr;

        private static final int THRESHOLD = 20;

        public CustomRecursiveTask(int[] arr) {
            this.arr = arr;
        }

        @Override
        protected Integer compute() {
            if (arr.length > THRESHOLD) {
                return ForkJoinTask.invokeAll(createSubTasks())
                    .stream()
                    .mapToInt(ForkJoinTask::join)
                    .sum();
            } else {
                return processing(arr);
            }
        }

        private Collection<CustomRecursiveTask> createSubTasks() {
            List<CustomRecursiveTask> dividedTasks = new ArrayList<>();
            dividedTasks.add(new CustomRecursiveTask(
                Arrays.copyOfRange(arr, 0, arr.length / 2)));
            dividedTasks.add(new CustomRecursiveTask(
                Arrays.copyOfRange(arr, arr.length / 2, arr.length)));
            return dividedTasks;
        }

        private Integer processing(int[] arr) {
            return Arrays.stream(arr)
                .filter(a -> a > 10 && a < 27)
                .map(a -> a * 10)
                .sum();
        }
    }

}
