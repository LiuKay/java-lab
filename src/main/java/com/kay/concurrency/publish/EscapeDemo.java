package com.kay.concurrency.publish;

import com.kay.concurrency.annotations.NotRecommend;
import com.kay.concurrency.annotations.NotThreadSafe;

/**
 * Created by kay on 2018/5/27.
 * <p>
 * 在对象未完成构造之前不可以将其发布
 * <p>
 * 对象逸出 在未完成初始化之前就暴露给了其他线程
 * <p>
 * this 引用逸出
 */
@NotThreadSafe
@NotRecommend
public class EscapeDemo {

    private int count = 0;

    public EscapeDemo() {
        new InnerClass();
        count = 555;
    }

    public static void main(String[] args) {
        new EscapeDemo();
    }

    private class InnerClass {

        public InnerClass() {
            System.out.println(EscapeDemo.this.count);
        }
    }
}
