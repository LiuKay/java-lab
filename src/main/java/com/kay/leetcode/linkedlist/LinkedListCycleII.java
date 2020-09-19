package com.kay.leetcode.linkedlist;

import com.kay.leetcode.ListNode;

/**
 * Created on 2020/5/23
 *
 * @author: LiuKay
 */
public class LinkedListCycleII {

		/**
		 * 双指针法： slow 每次走 1 步，走过的节点数为 s fast 每次走 2 步, 走过的节点数为 f 设 list head 到环入口的节点数为 a(不包含入口点), 环的长度为 b
		 * 第一次相遇时： 1. f=2s (因为fast 的速度为slow 的2倍) 2. f=s+ k*b (k>=1)
		 * <p>
		 * 2的推导过程如下： 设第一次相遇时，slow 和 fast 离开环的入口距离为 d,
		 * <p>
		 * 则 slow 走的路程为 s=a + d + n*b; (n 代表可能绕环的圈数) , 得 d = s-a-n*b fast 走的路程为 f = a + d + m*b. 得 d = f
		 * - a - m*b (m 为fast 绕圈数, m>n) => s-a-n*b = f- a -m*b => f = s + (m-n)*b => f= s + k*b
		 * <p>
		 * 由1，2 可得： s=k*b,即 slow 所走路程为环的 k倍 若此刻统计从 head 开始走的所有指针，到达环的入口所走的路程应该是 a+ k*b (即每绕环一圈 k+1),
		 * 由于此时 s=k*b,则可以让一个指针从 head 开始， 若到达 环入口，则 该指针必然与 slow 相遇在入口处
		 */
		public class Solution {

				public ListNode detectCycle(ListNode head) {
						ListNode fast = head, slow = head;
						while (fast != null && fast.next != null) {
								fast = fast.next.next;
								slow = slow.next;
								if (fast == slow) {
										while (head != slow) {
												head = head.next;
												slow = slow.next;
										}
										return head;
								}
						}
						return null;
				}
		}
}
