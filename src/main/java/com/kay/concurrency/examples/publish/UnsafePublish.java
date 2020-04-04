package com.kay.concurrency.examples.publish;

import com.kay.concurrency.annotations.NotThreadSafe;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * Created by kay on 2018/5/27.
 *
 * 不安全的对象发布
 * 通过public的方式获取到私有对象将其改变
 * 其他对象获取对象状态未知
 *
 */
@Log4j2
@NotThreadSafe
public class UnsafePublish {

    private String[] arr = {"aa", "bb", "cc"};

    public String[] getArr() {
        return arr;
    }

    public static void main(String[] args) {
        UnsafePublish unsafePublish = new UnsafePublish();

        log.info("arr:{}", Arrays.toString(unsafePublish.getArr()));

        unsafePublish.getArr()[1] = "change";

        log.info("arr:{}",Arrays.toString(unsafePublish.getArr()));
    }
}
