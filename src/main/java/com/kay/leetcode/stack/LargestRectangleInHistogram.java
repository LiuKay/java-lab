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
            Arrays.fill(right, n);// 将所有柱子的有边界预设为最后

            Stack<Integer> mono_stack = new Stack<>();
            //遍历一遍将每个元素的左右边界都确定下来
            for (int i = 0; i < n; ++i) {
                while (!mono_stack.isEmpty() && heights[mono_stack.peek()] >= heights[i]) {
                    // 确定栈顶元素的右边界
                    right[mono_stack.peek()] = i;
                    mono_stack.pop();
                }
                // 确定当前元素的左边界
                left[i] = (mono_stack.isEmpty() ? -1 : mono_stack.peek());
                mono_stack.push(i);
            }

            int ans = 0;
            for (int i = 0; i < n; ++i) {
                ans = Math.max(ans, (right[i] - left[i] - 1) * heights[i]);
            }
            return ans;
        }

        public int largestRectangleArea2(int[] heights) {
            Stack<Integer> stack = new Stack<>();
            stack.push(-1);// stack 存储每个柱子的下标
            int maxarea = 0;
            for (int i = 0; i < heights.length; ++i) {
                // 当前柱子大于stack 中的柱子时就push
                // 当栈顶柱子大于等于当前柱子时，说明当前柱子左边界还可以往左扩展
                // 需要注意的是为什么还可以是等于，因为此时栈顶柱子所占的面积肯定是小于当前柱子计算的面积的(因为宽度小于1)
                //所以出栈计算也是没问题的
                while (stack.peek() != -1 && heights[stack.peek()] >= heights[i]) {
                    maxarea = Math.max(maxarea, heights[stack.pop()] * (i - stack.peek() - 1));
                }
                stack.push(i);
            }
            while (stack.peek() != -1) {
                maxarea = Math.max(maxarea, heights[stack.pop()] * (heights.length - stack.peek() - 1));
            }
            return maxarea;
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] arr = new int[]{1, 0, 9};
        int area = solution.largestRectangleArea(arr);
        System.out.println(area);
    }
}
