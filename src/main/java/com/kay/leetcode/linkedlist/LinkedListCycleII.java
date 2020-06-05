package com.kay.leetcode.linkedlist;

import com.kay.leetcode.ListNode;

/**
 * Created on 2020/5/23
 *
 * @author: LiuKay
 */
public class LinkedListCycleII {

    public class Solution {
        public ListNode detectCycle(ListNode head) {
            ListNode fast = head, slow = head;
            while (true) {
                if (fast == null || fast.next == null) return null;
                fast = fast.next.next;
                slow = slow.next;
                if (fast == slow) break;
            }
            fast = head;
            while (slow != fast) {
                slow = slow.next;
                fast = fast.next;
            }
            return fast;
        }
    }
}
