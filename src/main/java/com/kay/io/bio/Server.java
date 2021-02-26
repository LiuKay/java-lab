package com.kay.io.bio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 基于 Java Socket 的基本通信： Server接收Client 消息打印在控制台，并回发给Client端 Created by kay on 2017/10/9.
 */
public class Server {

		public static void main(String[] args) throws IOException {
				ServerSocket serverSocket = new ServerSocket(8888);
				Socket socket = serverSocket.accept();
				System.out.println("Server is on accept...");
				DataInputStream inputStream = new DataInputStream(socket.getInputStream());
				String s = inputStream.readUTF();
				System.out.println("Server accept:" + s);

				DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
				outputStream.writeUTF(s);

				inputStream.close();
				outputStream.close();
				socket.close();
				serverSocket.close();
		}
}
