//Given two sorted integer arrays nums1 and nums2, merge nums2 into nums1 as one
// sorted array.
//
// Note:
//
//
// The number of elements initialized in nums1 and nums2 are m and n respectivel
//y.
// You may assume that nums1 has enough space (size that is greater or equal to
//m + n) to hold additional elements from nums2.
//
//
// Example:
//
//
//Input:
//nums1 = [1,2,3,0,0,0], m = 3
//nums2 = [2,5,6],       n = 3
//
//Output: [1,2,2,3,5,6]
// Related Topics Array Two Pointers
package com.kay.leetcode.arrary;

import java.util.Arrays;

/**
 * Created by LiuKay on 2020/5/28.
 */
public class MergeSortedArray {

  static class Solution {

    public int[] merge(int[] nums1, int m, int[] nums2, int n) {
      int k = m + n - 1; // all available length = m+n,  nums1.length >= m+n
      int i = m - 1;
      int j = n - 1;
      while (i >= 0 && j >= 0) {
        if (nums1[i] > nums2[j]) {
          nums1[k--] = nums1[i--];
        } else {
          nums1[k--] = nums2[j--];
        }
      }
      // cause nums1 is sorted, so if j<0 means all the elements in nums2 have merged into nums1
      while (j >= 0) {
        nums1[k--] = nums2[j--];
      }
      return nums1;
    }
  }

  public static void main(String[] args) {

    int[] arr1 = new int[]{1, 2, 3, 0, 0, 0};
    int[] arr2 = new int[]{2, 5, 6};
    Solution solution = new Solution();
    int[] merge = solution.merge(arr1, 3, arr2, 3);

    Arrays.stream(merge).forEach(System.out::println);
  }
}
