package com.kay.io.nio.timesocket;

/**
 * Created by kay on 2017/9/8.
 */
public class TimeCient {

		public static void main(String[] args) {
				int port = 8888;
				TimeClientHandle clientHandle = new TimeClientHandle("127.0.0.1", port);
				new Thread(clientHandle, "TimeClient").start();

		}
}
