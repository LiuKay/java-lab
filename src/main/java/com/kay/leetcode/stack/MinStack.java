package com.kay.leetcode.stack;

import java.util.Stack;

public class MinStack {
    private final Stack<Integer> stack;

    private final Stack<Integer> minStack;

    /** initialize your data structure here. */
    public MinStack() {
        stack=new Stack<>();
        minStack=new Stack<>();
    }

    public void push(int x) {
        stack.push(x);
        if(minStack.empty() || minStack.peek()>= x){
            minStack.push(x);
        }
    }

    public void pop() {
        Integer pop = stack.pop();
        if (pop != null && pop.equals(minStack.peek())) {
            minStack.pop();
        }
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }
}
