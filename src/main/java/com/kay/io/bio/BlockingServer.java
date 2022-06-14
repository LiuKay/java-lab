package com.kay.io.bio;

import lombok.extern.log4j.Log4j2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Blocking Socket I/O Demo Start server first, run Client later
 */
@Log4j2
public class BlockingServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        log.info("Server is on accept...");
        // server is blocking on accept()
        Socket socket = serverSocket.accept();

        //connected
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());

        //block reading
        String s = inputStream.readUTF();
        log.info("Server Read:" + s);

        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        //block writing
        outputStream.writeUTF(s);

        inputStream.close();
        outputStream.close();
        socket.close();
        serverSocket.close();
    }
}
