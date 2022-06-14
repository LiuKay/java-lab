package com.kay.io.bio;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

@Log4j2
public class UDPDemo {


    public static void main(String[] args) throws IOException {
        UDPServer udpServer = new UDPServer();
        new Thread(udpServer).start();

        UDPClient udpClient = new UDPClient();
        udpClient.sendMessage("hello world!");
        udpClient.sendMessage("end");
        udpServer.stop();
    }

    static class UDPServer implements Runnable {
        private final byte[] buffer = new byte[256];
        private volatile boolean run = true;

        public void stop() {
            this.run = false;
        }

        @SneakyThrows
        @Override
        public void run() {
            DatagramSocket datagramSocket = new DatagramSocket(9999);
            while (run) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                System.err.println("server is trying to receive packet...");

                datagramSocket.receive(packet);

                InetAddress clientAddress = packet.getAddress();
                int port = packet.getPort();

                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, clientAddress, port);
                String received = new String(packet.getData(), 0, packet.getLength());
                if (received.equals("end")) {
                    datagramSocket.send(datagramPacket);
                    run = false;
                    continue;
                }
                datagramSocket.send(datagramPacket);
            }
            datagramSocket.close();
            System.err.println("server closed.");
        }
    }

    static class UDPClient {
        private DatagramSocket socket;
        private InetAddress address;
        private byte[] buffer;

        void sendMessage(String message) throws IOException {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
            buffer = message.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 9999);
            socket.send(packet);

            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            log.info("client received:" + received);

            if (received.equals("end")) {
                socket.close();
                log.info("client closed.");
            }

        }

    }


}
