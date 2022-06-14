package com.kay.concurrency.publish;

import com.kay.concurrency.annotations.NotThreadSafe;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

/**
 * Created by kay on 2018/5/27.
 * <p>
 * 不安全的对象发布 通过public的方式获取到私有对象将其改变 其他对象获取对象状态未知
 */
@Log4j2
@NotThreadSafe
public class UnsafePublishDemo {

    private final String[] arr = {"aa", "bb", "cc"};

    public static void main(String[] args) {
        UnsafePublishDemo unsafePublishDemo = new UnsafePublishDemo();

        log.info("arr=" + Arrays.toString(unsafePublishDemo.getArr()));

        unsafePublishDemo.getArr()[1] = "change";

        log.info("arr=" + Arrays.toString(unsafePublishDemo.getArr()));
    }

    public String[] getArr() {
        return arr;
    }
}
