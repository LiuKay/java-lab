package com.kay.concurrency.publish;

import com.kay.concurrency.annotations.NotRecommend;
import com.kay.concurrency.annotations.NotThreadSafe;

/**
 * Created by kay on 2018/5/27.
 *
 * 在对象未完成构造之前不可以将其发布
 *
 * 对象逸出
 * 在未完成初始化之前就暴露给了其他线程
 *
 * this 引用逸出
 */
@NotThreadSafe
@NotRecommend
public class Escape {

    private int count = 0;

    public Escape() {
        new InnerClass();
        count = 555;
    }

    private class InnerClass{

        public InnerClass() {
            System.out.println(Escape.this.count);
        }
    }

    public static void main(String[] args) {
        new Escape();
    }
}
