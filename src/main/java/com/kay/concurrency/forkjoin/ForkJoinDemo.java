package com.kay.concurrency.forkjoin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import lombok.extern.log4j.Log4j2;

/**
 * <a href="http://gee.cs.oswego.edu/dl/papers/fj.pdf"> <i>A Java Fork/Join Framework</i></a>
 */
@Log4j2
public class ForkJoinDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinDemo demo = new ForkJoinDemo();
//        demo.testRecursiveTask();
        demo.testWordCount();
    }

    void testRecursiveTask() throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool(4);
        Fibonacci fibonacci = new Fibonacci(30);
        pool.submit(fibonacci);

        Integer integer = fibonacci.get();
        System.out.println(integer);
    }

    class Fibonacci extends RecursiveTask<Integer> {
        final int n;
        Fibonacci(int n) { this.n = n; }

        @Override
        protected Integer compute() {
            if (n <= 1)
                return n;
            Fibonacci f1 = new Fibonacci(n - 1);
            f1.fork(); // 异步子任务执行
            Fibonacci f2 = new Fibonacci(n - 2);
            return f2.compute() + f1.join(); // 阻塞等待 f1 执行完
        }
    }

    void testWordCount() {
        String[] lines = new String[]{
            "hello world",
            "hello kay",
            "test hello",
            "kay hi"
        };
        ForkJoinPool pool = new ForkJoinPool(4, NamedForkJoinFactory.create("WordCount"), null,
            false);
        WordCount wordCount = new WordCount(lines, 0, lines.length);
        Map<String, Long> result = pool.invoke(wordCount);

        result.forEach((key, value) -> System.out.println(key + ":" + value));
    }

    class WordCount extends RecursiveTask<Map<String,Long>>{
        private String[] lines;
        private int start;
        private int end;

        public WordCount(String[] lines, int start, int end) {
            this.lines = lines;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Map<String, Long> compute() {
            if (end - start == 1) {
                return calculate(lines[start]); //前闭后开，对应 0~lines.length
            }
            int mid = (end + start) / 2;
            WordCount w1 = new WordCount(lines, start, mid);
            w1.fork();
            WordCount w2 = new WordCount(lines, mid, end);
            return merge(w2.compute(), w1.join());
        }

        private Map<String,Long> merge(Map<String, Long> r2, Map<String, Long> r1){
            log.info("merge");
            Map<String, Long> result = new HashMap<>(r2);
            r1.forEach((key,count)->{
                if (result.containsKey(key)) {
                    result.put(key, result.get(key) + count);
                }else {
                    result.put(key, count);
                }
            });
            return result;
        }

        private Map<String, Long> calculate(String line) {
            log.info("calculate,line={}", line);
            HashMap<String, Long> map = new HashMap<>();
            String[] split = line.split(" ");
            for (String s : split) {
                if (map.containsKey(s)) {
                    map.put(s, map.get(s) + 1L);
                }else {
                    map.put(s, 1L);
                }
            }
            return map;
        }

    }
}
