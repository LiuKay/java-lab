package com.kay.leetcode.stack;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by LiuKay on 2020/6/11.
 */
public class ValidParentheses {

  static class Solution {

    private static Map<Character, Character> map = new HashMap<>();

    static {
      map.put(')', '(');
      map.put(']', '[');
      map.put('}', '{');
    }

    public boolean isValid(String s) {
      Stack<Character> stack = new Stack<>();
      char[] arr = s.toCharArray();
      for (char str : arr) {
        if (map.containsKey(str)) {
          char top = stack.isEmpty() ? '?' : stack.pop();
          if (top != map.get(str)) {
            return false;
          }
        } else {
          stack.push(str);
        }
      }
      return stack.isEmpty();
    }
  }

  public static void main(String[] args) {
    Solution solution = new Solution();
    System.out.println(solution.isValid("{[]}"));
  }
}