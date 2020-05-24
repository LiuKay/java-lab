//Given a sorted array nums, remove the duplicates in-place such that each eleme
//nt appear only once and return the new length.
//
// Do not allocate extra space for another array, you must do this by modifying
//the input array in-place with O(1) extra memory.
//
// Example 1:
//
//
//Given nums = [1,1,2],
//
//Your function should return length = 2, with the first two elements of nums be
//ing 1 and 2 respectively.
//
//It doesn't matter what you leave beyond the returned length.
//
// Example 2:
//
//
//Given nums = [0,0,1,1,1,2,2,3,3,4],
//
//Your function should return length = 5, with the first five elements of nums b
//eing modified to 0, 1, 2, 3, and 4 respectively.
//
//It doesn't matter what values are set beyond the returned length.
//
//
// Clarification:
//
// Confused why the returned value is an integer but your answer is an array?
//
// Note that the input array is passed in by reference, which means modification
// to the input array will be known to the caller as well.
//
// Internally you can think of this:
//
//
//// nums is passed in by reference. (i.e., without making a copy)
//int len = removeDuplicates(nums);
//
//// any modification to nums in your function would be known by the caller.
//// using the length returned by your function, it prints the first len element
//s.
//for (int i = 0; i < len; i++) {
//    print(nums[i]);
//} Related Topics Array Two Pointers
package com.kay.leetcode.arrary;

/**
 * Created on 2020/5/24
 *
 * @author: LiuKay
 */
public class RemoveDuplicates {

    class Solution {

        public int removeDuplicates(int[] nums) {
            if (nums == null || nums.length ==0) {
                return 0;
            }
            int i = 0; // 指向最后一个不重复元素的index
            int j = i + 1;
            while (j < nums.length) {
                if (nums[i] != nums[j]) {
                    nums[++i] = nums[j]; // 遇到不重复的元素直接往后面怼
                }
                // 重复的直接跳过
                j++;
            }
            return i + 1;
        }
    }
}
