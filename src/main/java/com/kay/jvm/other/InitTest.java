package com.kay.jvm.other;

/**
 * Created by kay on 2018/5/28.
 */
public class InitTest {

  private static String mm = new String("ds");

  private static int count = 1;

  static {
    count++;
    System.out.println(count);
    System.out.println("static");
  }

  public InitTest() {
    count++;
    System.out.println("construct");
    System.out.println(count);
  }

  public static void main(String[] args) {
    new InitTest();
  }


}
