//Given n non-negative integers a1, a2, ..., an , where each represents a point
//at coordinate (i, ai). n vertical lines are drawn such that the two endpoints of
// line i is at (i, ai) and (i, 0). Find two lines, which together with x-axis for
//ms a container, such that the container contains the most water.
//
// Note: You may not slant the container and n is at least 2.
//
//
//
//
//
// The above vertical lines are represented by array [1,8,6,2,5,4,8,3,7]. In thi
//s case, the max area of water (blue section) the container can contain is 49.
//
//
//
// Example:
//
//
//Input: [1,8,6,2,5,4,8,3,7]
//Output: 49 Related Topics Array Two Pointers

package com.kay.leetcode.arrary;

/**
 * Created on 2020/5/22
 *
 * @author: LiuKay
 */
public class ContainerWithMostWater {

  class Solution {

    //1. 暴力解法
    public int maxArea(int[] height) {
      int max = 0;
      for (int i = 0; i < height.length - 1; i++) {
        for (int j = i + 1; j < height.length; j++) {
          int h = Math.min(height[i], height[j]);
          int area = (j - i) * h;
          max = Math.max(area, max);
        }
      }
      return max;
    }

    //2.左右边界，向中间收敛
    public int maxArea2(int[] height) {
      int max = 0;
      for (int i = 0, j = height.length - 1; i < j; ) {
        int minHeight = height[i] > height[j] ? height[j--] : height[i++];
        int area = minHeight * (j - i + 1);
        max = Math.max(area, max);
      }
      return max;
    }
  }
}
