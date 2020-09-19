package com.kay.leetcode.arrary;

/**
 * Created on 2020/5/19
 *
 * @author: LiuKay
 */
public class AddTwoNumbers {

		//You are given two non-empty linked lists representing two non-negative integer
//s. The digits are stored in reverse order and each of their nodes contain a sing
//le digit. Add the two numbers and return it as a linked list.
//
// You may assume the two numbers do not contain any leading zero, except the nu
//mber 0 itself.
//
// Example:
//Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
//Output: 7 -> 0 -> 8
//Explanation: 342 + 465 = 807.
//
// Related Topics Linked List Math
		static class ListNode {

				int val;
				ListNode next;

				ListNode() {
				}

				ListNode(int val) {
						this.val = val;
				}

				ListNode(int val, ListNode next) {
						this.val = val;
						this.next = next;
				}
		}

		static class Solution {

				public ListNode addTwoNumbers1(ListNode l1, ListNode l2) {
						ListNode head = new ListNode(0);

						ListNode cur = head;
						int sum = 0;
						while (l1 != null || l2 != null) {
								sum = sum / 10; // carry
								if (l1 != null) {
										sum += l1.val;
										l1 = l1.next;
								}
								if (l2 != null) {
										sum += l2.val;
										l2 = l2.next;
								}

								cur.next = new ListNode(sum % 10);
								cur = cur.next; // set next node as cur
						}
						if (sum >= 10) {
								cur.next = new ListNode(1);
						}
						return head.next;
				}

				public ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
						ListNode prev = new ListNode(0);
						ListNode head = prev;
						int carry = 0;
						while (l1 != null || l2 != null || carry != 0) {
								ListNode cur = new ListNode(0);
								int sum = (l1 == null ? 0 : l1.val) + (l2 == null ? 0 : l2.val) + carry;
								cur.val = sum % 10;
								carry = sum / 10;

								prev.next = cur; //link to cur
								prev = cur;  //move to cur

								l1 = l1 == null ? null : l1.next;
								l2 = l2 == null ? null : l2.next;
						}
						return head.next;
				}
		}

}
