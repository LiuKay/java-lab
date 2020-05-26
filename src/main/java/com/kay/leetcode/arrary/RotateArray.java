package com.kay.leetcode.arrary;

/**
 * Created by LiuKay on 2020/5/26.
 */
public class RotateArray {

    static class Solution {

        // time:O(n*k) space:O(1)
        public void rotate1(int[] nums, int k) {
            for (int i = 0; i < k; i++) {
                int last = nums[nums.length - 1];
                for (int j = 0; j < nums.length; j++) {
                    int t = nums[j];
                    nums[j] = last;
                    last = t;
                }
            }
        }

        //time:O(n) space:O(n)
        public void rotate2(int[] nums, int k) {
            int[] arr = new int[nums.length];
            for (int i = 0; i < nums.length; i++) {
                arr[(i + k) % nums.length] = nums[i];
            }
            for (int i = 0; i < nums.length; i++) {
                nums[i] = arr[i];
            }
        }

        //time:O(n) space:O(1)
        // input: "----->-->" output: "-->----->"
        // step1: "----->-->" => "<--<-----"
        // step2:             => "--><-----"
        // step3:             => "-->----->"
        public void rotate3(int[] nums, int k) {
            k %= nums.length;// in case of k>length
            reverse(nums, 0, nums.length - 1);
            reverse(nums, 0, k - 1);
            reverse(nums, k, nums.length - 1);
        }

        void reverse(int[] arr, int start, int end) {
            while (start < end) {
                int temp = arr[start];
                arr[start] = arr[end];
                arr[end] = temp;
                start++;
                end--;
            }
        }

        //time:O(n) space:O(1)
        //move n times,
        public void rotate4(int[] nums, int k) {
            k = k % nums.length;
            int count = 0; // count for move times
            for (int start = 0; count < nums.length; start++) {
                int current = start;
                int prev = nums[start];
                do {
                    int next = (current + k) % nums.length;//current move to next
                    int temp = nums[next];
                    nums[next] = prev;
                    prev = temp;
                    current = next;
                    count++;
                } while (start != current); //if not back to original position
            }
        }


        public static void main(String[] args) {
            int[] nums = new int[]{1, 2, 3, 4, 5, 6, 7};
//            Solution solution = new Solution();
//            solution.rotate1(nums, 3);

//            for (int i = 0; i < nums.length; i++) {
//                System.out.println(i + ":" + ((i+2) % nums.length));
//            }

            int k = 8;
            k %= nums.length;
            System.out.println(k);
        }
    }
}
