//Merge two sorted linked lists and return it as a new list. The new list should
// be made by splicing together the nodes of the first two lists.
//
// Example:
//
//Input: 1->2->4, 1->3->4
//Output: 1->1->2->3->4->4
//
// Related Topics Linked List
package com.kay.leetcode.linkedlist;

import com.kay.leetcode.ListNode;

/**
 * Created by LiuKay on 2020/5/27.
 */
public class MergeTwoSortedList {

  static class Solution {

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
      ListNode head = new ListNode(-1);
      ListNode cur = head;
      while (l1 != null && l2 != null) {
        if (l1.val < l2.val) {
          cur.next = l1;
          l1 = l1.next;
        } else {
          cur.next = l2;
          l2 = l2.next;
        }
        cur = cur.next;
      }
      cur.next = l1 == null ? l2 : l1;
      return head.next;
    }
  }

  public static void main(String[] args) {
    ListNode l1 = ListNode.of(new int[]{1, 2, 3});
    ListNode l2 = ListNode.of(new int[]{1, 3, 4});

    Solution solution = new Solution();
    ListNode listNode = solution.mergeTwoLists(l1, l2);
  }

}
