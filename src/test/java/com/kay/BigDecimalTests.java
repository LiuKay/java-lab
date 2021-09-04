package com.kay;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

public class BigDecimalTests {

    @Test
    void test1() {
        float f1 = 1.0f - 0.9f;
        float f2 = 0.9f - 0.8f;

        assertThat(f1 == f2).isTrue();
    }

    @Test
    void test2() {
        BigDecimal a = new BigDecimal("0.1");
        BigDecimal b = new BigDecimal("0.9");
        BigDecimal c = new BigDecimal("0.8");

        final BigDecimal subtract = b.subtract(c);

        assertThat(subtract).isEqualTo(a);
    }

    @Test
    void test3() {
        BigDecimal m = new BigDecimal("1.255433");
        BigDecimal n = m.setScale(3, RoundingMode.HALF_DOWN);

        assertThat(n.toString()).isEqualTo("1.255");
    }
}
