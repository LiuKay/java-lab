package com.kay.leetcode;

/**
 * Created by LiuKay on 2020/5/27.
 */
public class ListNode {

  public int val;
  public ListNode next;

  public ListNode() {
  }

  public ListNode(int val) {
    this.val = val;
  }

  public ListNode(int val, ListNode next) {
    this.val = val;
    this.next = next;
  }

  public static ListNode of(int... arr) {
    if (arr == null || arr.length == 0) {
      return null;
    }
    ListNode head = new ListNode(arr[0]);
    ListNode cur = head;
    int i = 1;
    while (i < arr.length) {
      ListNode next = new ListNode(arr[i]);
      cur.next = next;
      cur = cur.next;
      i++;
    }
    return head;
  }

  public static void print(ListNode head) {
    System.out.print("[");
    while (head != null) {
      System.out.print(head.val);
      head = head.next;
      if (head != null) {
        System.out.print(",");
      }
    }
    System.out.print("]");
  }
}
