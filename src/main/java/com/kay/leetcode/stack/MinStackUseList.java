package com.kay.leetcode.stack;

class MinStackUseList {

  private static class Node {

    int val;
    int min;
    Node next;

    public Node(int val, int min) {
      this(val, min, null);
    }

    public Node(int val, int min, Node next) {
      this.val = val;
      this.min = min;
      this.next = next;
    }
  }

  private Node head;

  public MinStackUseList() {
  }

  void push(int x) {
    if (head == null) {
      head = new Node(x, x);
    } else {
      head = new Node(x, Math.min(x, head.min), head);
    }
  }

  void pop() {
    head = head.next;
  }

  int top() {
    return head.val;
  }

  int getMin() {
    return head.min;
  }
}
