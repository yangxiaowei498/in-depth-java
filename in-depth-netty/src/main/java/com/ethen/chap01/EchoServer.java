package com.ethen.chap01;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class EchoServer {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket();
        InetSocketAddress address = new InetSocketAddress(10001);
        server.bind(address);
        System.err.println("server is started !!!");
        // 7X24营业
        while (true) {
            final Socket accept = server.accept();
            new Thread(new EchoServerTask(accept)).start();
        }
    }

    static class EchoServerTask implements Runnable {
        private final Socket socket;

        public EchoServerTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            final SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
            System.out.println("remoteSocketAddress:" + remoteSocketAddress);
            try (InputStream input = socket.getInputStream();
                 ObjectInputStream ois = new ObjectInputStream(input);
                 OutputStream output = socket.getOutputStream();
                 final ObjectOutputStream oos = new ObjectOutputStream(output)
            ) {
                final String s = ois.readUTF();
                System.out.println("接收到客户端的请求消息：[" + s + "]");
                oos.writeUTF("hello," + s);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}