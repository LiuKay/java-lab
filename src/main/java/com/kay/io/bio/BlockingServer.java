package com.kay.io.bio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Blocking Socket I/O Demo Start server first, run Client later
 */
public class BlockingServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("Server is on accept...");
        // server is blocking on accept()
        Socket socket = serverSocket.accept();

        //connected
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());

        //block reading
        String s = inputStream.readUTF();
        System.out.println("Server Read:" + s);

        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        //block writing
        outputStream.writeUTF(s);

        inputStream.close();
        outputStream.close();
        socket.close();
        serverSocket.close();
    }
}
