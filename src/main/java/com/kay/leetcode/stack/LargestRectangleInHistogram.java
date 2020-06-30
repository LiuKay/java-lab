/**
 * Given n non-negative integers representing the histogram's bar height where the width of each bar is 1, find the area of largest rectangle in the histogram.
 * Input: [2,1,5,6,2,3]
 * Output: 10
 */
package com.kay.leetcode.stack;

import java.util.Arrays;
import java.util.Stack;

public class LargestRectangleInHistogram {
    static class Solution {
        // 1. 暴力法
        public int largestRectangleArea(int[] heights) {
            int size = heights.length;
            int area = 0;
            for (int i = 0; i < size; i++) {
                int h = Integer.MAX_VALUE;
                for (int j = i; j < size; j++) {
                    h = Math.min(h, heights[j]);
                    int w = j - i + 1;
                    area = Math.max(area, w * h);
                }
            }
            return area;
        }

        //2. stack
        public int largestRectangleAreaByStack(int[] heights) {
            int n = heights.length;
            int[] left = new int[n];
            int[] right = new int[n];
            Arrays.fill(right, n);

            Stack<Integer> mono_stack = new Stack<>();
            //遍历一遍将每个元素的左右边界都确定下来
            for (int i = 0; i < n; ++i) {
                while (!mono_stack.isEmpty() && heights[mono_stack.peek()] >= heights[i]) {
                    right[mono_stack.peek()] = i;
                    mono_stack.pop();
                }
                left[i] = (mono_stack.isEmpty() ? -1 : mono_stack.peek());
                mono_stack.push(i);
            }

            int ans = 0;
            for (int i = 0; i < n; ++i) {
                ans = Math.max(ans, (right[i] - left[i] - 1) * heights[i]);
            }
            return ans;
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] arr = new int[]{1, 0, 9};
        int area = solution.largestRectangleArea(arr);
        System.out.println(area);
    }
}
