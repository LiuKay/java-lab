package com.kay.leetcode.linkedlist;

import com.kay.leetcode.ListNode;

/**
 * Created on 2020/5/23
 *
 * @author: LiuKay
 */
public class ReverseLinkedList {


		public ListNode reverseList(ListNode head) {
				ListNode cur = head;
				ListNode prev = null;
				while (cur != null) {
						ListNode next = cur.next;
						cur.next = prev;
						prev = cur;
						cur = next;
				}
				return prev;
		}

		public ListNode reverseListRecursive(ListNode head) {
				if (head == null || head.next == null) {
						return head;
				}

				ListNode p = reverseListRecursive(head.next);
				// A->B->C->D<-E<-F , if you are at C ,
				head.next.next = head;
				head.next = null;
				return p;
		}

		public static void main(String[] args) {
				ListNode head = ListNode.of(new int[]{1, 2, 3});

				ReverseLinkedList demo = new ReverseLinkedList();
				ListNode node = demo.reverseListRecursive(head);
				ListNode.print(node);
		}

}
