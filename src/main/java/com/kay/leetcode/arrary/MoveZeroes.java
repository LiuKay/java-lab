package com.kay.leetcode.arrary;

/**
 * Created on 2020/5/21
 *
 * @author: LiuKay
 */
public class MoveZeroes {

//Given an array nums, write a function to move all 0's to the end of it while m
//aintaining the relative order of the non-zero elements.
//
// Example:
//
//
//Input: [0,1,0,3,12]
//Output: [1,3,12,0,0]
//
// Note:
//
//
// You must do this in-place without making a copy of the array.
// Minimize the total number of operations.
// Related Topics Array Two Pointers

    class Solution {

        public void moveZeroes(int[] nums) {
            int nextPos = 0; // next non-zero position
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] != 0) {
                    int t = nums[i];
                    nums[i] = nums[nextPos];
                    nums[nextPos++] = t;
                }
            }
        }
    }
}
