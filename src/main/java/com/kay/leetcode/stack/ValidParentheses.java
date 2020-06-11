package com.kay.leetcode.stack;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by LiuKay on 2020/6/11.
 */
public class ValidParentheses {

    class Solution {
        public boolean isValid(String s) {
            Map<Character,Character> map=new HashMap<>();
            map.put(')','(');
            map.put(']','[');
            map.put('}','{');

            Stack<Character> stack=new Stack<>();
            char[] arr=s.toCharArray();
            for(char str : arr){
                if(map.containsKey(str)){
                    char top= stack.isEmpty() ? '?' : stack.pop();
                    if(top !=map.get(str)){
                        return false;
                    }else{
                        stack.push(str);
                    }
                }
            }
            return stack.isEmpty();
        }
    }
}
