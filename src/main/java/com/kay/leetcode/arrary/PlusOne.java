package com.kay.leetcode.arrary;

/**
 * Created by LiuKay on 2020/5/30.
 */
public class PlusOne {

    class Solution {
        public int[] plusOne(int[] digits) {
            for (int i = digits.length - 1; i >= 0; i--) {
                if (digits[i] < 9) {
                    digits[i]++;
                    return digits;
                }

                digits[i] = 0;
            }

            int[] res = new int[digits.length + 1];
            res[0] = 1; // the default value for int is 0!!!!!
            return res;
        }
    }
}
