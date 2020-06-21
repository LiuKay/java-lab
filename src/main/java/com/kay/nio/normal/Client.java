package com.kay.nio.normal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by kay on 2017/10/9.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8888);
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.writeUTF("kay is on the Socket...");

        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        String s = inputStream.readUTF();

        System.out.println("client accept:"+s);

        outputStream.close();
        inputStream.close();
        socket.close();
    }
}
