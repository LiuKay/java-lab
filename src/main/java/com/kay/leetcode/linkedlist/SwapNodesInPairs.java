package com.kay.leetcode.linkedlist;

import com.kay.leetcode.ListNode;

/**
 * Created on 2020/5/23
 *
 * @author: LiuKay
 */
public class SwapNodesInPairs {

  static class Solution {

    /**
     *
     */
    public ListNode swapPairs(ListNode head) {
      if (head == null || head.next == null) {
        return head;
      }
      ListNode cur = head;
      ListNode newHead = head.next;
      while (cur != null && cur.next != null) {
        ListNode prev = cur;
        cur = cur.next;
        prev.next = cur.next; // store next.next
        cur.next = prev;  // reverse
        cur = prev.next; //move to original next.next

        if (cur != null && cur.next != null) {
          prev.next = cur.next;
        }
      }
      return newHead;
    }

    public ListNode recursiveSwapPairs(ListNode head) {
      if (head == null || head.next == null) {
        return head;
      }
      ListNode next = head.next;
      ListNode p = recursiveSwapPairs(next.next);
      head.next = p;
      next.next = head;
      return next;
    }
  }

  public static void main(String[] args) {
    Solution solution = new Solution();
    ListNode listNode = solution.swapPairs(ListNode.of(1, 2, 3, 4));
    ListNode.print(listNode);
  }
}
