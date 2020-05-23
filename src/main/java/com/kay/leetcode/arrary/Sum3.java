//Given an array nums of n integers, are there elements a, b, c in nums such tha
//t a + b + c = 0? Find all unique triplets in the array which gives the sum of ze
//ro.
//
// Note:
//
// The solution set must not contain duplicate triplets.
//
// Example:
//
//
//Given array nums = [-1, 0, 1, 2, -1, -4],
//
//A solution set is:
//[
//  [-1, 0, 1],
//  [-1, -1, 2]
//]
//
// Related Topics Array Two Pointers
package com.kay.leetcode.arrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.util.Pair;

/**
 * Created on 2020/5/20
 *
 * @author: LiuKay
 */
public class Sum3 {

    /**
     * Solution:
     * 1. Two pointers
     * Algorithm
     *
     * The implementation is straightforward - we just need to modify twoSumII to produce triplets and skip repeating values.
     *
     * For the main function:
     *
     * Sort the input array nums.
     * Iterate through the array:
     * If the current value is greater than zero, break from the loop. Remaining values cannot sum to zero.
     * If the current value is the same as the one before, skip it.
     * Otherwise, call twoSumII for the current position i.
     * For twoSumII function:
     *
     * Set the low pointer lo to i + 1, and high pointer hi to the last index.
     * While low pointer is smaller than high:
     * If the sum of nums[i], nums[lo] and nums[hi] is less than zero, increment lo.
     * Also increment lo if the value is the same as for lo - 1.
     * If the sum is greater than zero, decrement hi.
     * Also decrement hi if the value is the same as for hi + 1.
     * Otherwise, we found a triplet:
     * Add it to the result res.
     * Decrement hi and increment lo.
     * Return the result res.
     *
     *
     * 2.Hash Set
     * Since triplets must sum up to the target value, we can try the hash table approach from the Two Sum solution. This approach won't work, however, if the sum needs to be smaller than a target, like in 3Sum Smaller.
     *
     * Handling duplicates here is trickier compared to the two pointers approach. We can put a combination of three values into a hash set to efficiently check whether we've found that triplet already. Values in a combination should be ordered (e.g. ascending). Otherwise, we can have results with the same values in the different positions.
     *
     * Fortunately, we do not need to store all three values - storing the smallest and the largest ones is sufficient to identify any triplet. Because three values sum to the target, the third value will always be the same.
     *
     * Algorithm
     *
     * We process each value from left to right. For value v, we need to find all pairs whose sum is equal -v. To find such pairs, we apply the Two Sum: One-pass Hash Table approach to the rest of the array. To ensure unique triplets, we use a hash set found as described above.
     *
     * Because hashmap operations could be expensive, the solution below may be too slow.
     *
     * =>Optimized Algorithm
     *
     * These optimizations don't change the big-O complexity, but help speed things up:
     *
     * Use another hash set dups to skip duplicates in the outer loop.
     * Instead of re-populating a hash set every time in the inner loop, we can populate a hashmap once and then only modify values. After we process nums[j] in the inner loop, we set the hashmap value to i. This indicates that we can now use nums[j] to find pairs for nums[i].
     */

    class Solution1 {
        public List<List<Integer>> threeSum(int[] nums) {
            Arrays.sort(nums);
            List<List<Integer>> result = new LinkedList<>();
            for (int i = 0; i < nums.length && nums[i] <= 0; i++) { //sorted, if nums[i]>0, then will be no result.
                if (i == 0 || nums[i] != nums[i - 1]) {
                    twoSum(nums, result, i);
                }
            }
            return result;
        }

        private void twoSum(int[] nums, List<List<Integer>> result, int i) {
            int lo = i + 1;
            int hi = nums.length - 1;
            while (lo < hi) {
                int sum = nums[i] + nums[lo] + nums[hi];
                // lo > i+1 means the element after lo should not repeat lo cause lo start from i+1.
                if (sum < 0 || (lo > i + 1 && nums[lo] == nums[lo - 1])) {
                    lo++;
                } else if (sum > 0 || (hi < nums.length - 1 && nums[hi] == nums[hi + 1])) {
                    hi--;
                } else {
                    result.add(Arrays.asList(nums[i], nums[lo++], nums[hi--]));
                }
            }
        }
    }

    class Solution2{
        public List<List<Integer>> threeSum(int[] nums) {
            List<List<Integer>> res = new ArrayList<>();
            Set<Pair<Integer,Integer>> set = new HashSet<>();
            Set<Integer> dups = new HashSet<>();
            Map<Integer, Integer> seen = new HashMap<>();
            for (int i = 0; i < nums.length; i++) {
                if (dups.add(nums[i])) {
                    for (int j = i + 1; j < nums.length; j++) {
                        int target = -nums[i] - nums[j];
                        if (seen.containsKey(target) && seen.get(target).equals(i)) {
                            int v1 = Math.min(nums[i], Math.min(nums[j], target));
                            int v2 = Math.max(nums[i], Math.max(nums[j], target));
                            if (set.add(new Pair(v1, v2))) {
                                res.add(Arrays.asList(nums[i], nums[j], target));
                            }
                        }
                        seen.put(nums[j], i);
                    }
                }
            }
            return res;
        }
    }

}
