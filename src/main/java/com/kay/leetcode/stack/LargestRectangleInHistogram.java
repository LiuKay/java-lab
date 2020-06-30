/**
 * Given n non-negative integers representing the histogram's bar height where the width of each bar is 1, find the area of largest rectangle in the histogram.
 * Input: [2,1,5,6,2,3]
 * Output: 10
 */
package com.kay.leetcode.stack;

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

    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] arr = new int[]{1, 0, 9};
        int area = solution.largestRectangleArea(arr);
        System.out.println(area);
    }
}
