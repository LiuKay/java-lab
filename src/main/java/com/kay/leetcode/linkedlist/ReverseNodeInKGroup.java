package com.kay.leetcode.linkedlist;

import com.kay.leetcode.ListNode;

/**
 * Created on 2020/5/23
 *
 * @author: LiuKay
 */
public class ReverseNodeInKGroup {

		/**
		 * dummyNode-1-2-3-4-5
		 * <p>
		 * dummyNode-3-2-1-4-5
		 */
		class Solution {

				public ListNode reverseKGroup(ListNode head, int k) {
						if (head == null || head.next == null || k == 1) {
								return head;
						}
						ListNode dummyNode = new ListNode(-1);
						dummyNode.next = head;

						ListNode begin = dummyNode;
						int i = 0;
						while (head != null) {
								i++;
								if (i % k == 0) {
										//begin is the end of reversed nodes
										begin = reverse(begin, head.next);
										head = begin.next;
								} else {
										head = head.next;
								}
						}
						return dummyNode.next;
				}

				/**
				 * 1. internal reverse 2. outer links
				 */
				private ListNode reverse(ListNode begin, ListNode end) {
						ListNode cur = begin.next;
						ListNode prev = begin;
						ListNode first = cur; // for next begin
						while (cur != end) {
								ListNode t = cur.next;
								cur.next = prev;
								prev = cur;
								cur = t;
						}
						first.next = cur; // for the reversed nodes,the first node will be the end one, and linked to next start
						begin.next = prev;
						return first;
				}
		}
}
