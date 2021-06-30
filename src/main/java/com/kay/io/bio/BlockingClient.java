package com.kay.io.bio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by kay on 2017/10/9.
 */
public class BlockingClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8888);
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        //blocking
        outputStream.writeUTF("Hello");

        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        //blocking
        String s = inputStream.readUTF();

        System.out.println("Client Accept:" + s);

        outputStream.close();
        inputStream.close();
        socket.close();
    }
}
