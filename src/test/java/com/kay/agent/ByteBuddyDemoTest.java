package com.kay.agent;

import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ByteBuddyDemoTest {

    @Test
    @SneakyThrows
    void helloByteBuddyToString() {
        Class<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.isToString())
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded();

        String word = dynamicType.getDeclaredConstructor().newInstance().toString();

        assertThat(word).isEqualTo("Hello World!");
    }
}