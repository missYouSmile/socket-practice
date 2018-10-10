package com.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * socket server starter
 */
public class ServerStart {

    private static long id = 0L;

    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(8080);

        while (true) {
            Socket client = server.accept();
            new OnLineClient(createId(), client).start();
        }

    }

    private static long createId() {
        return ++id;
    }

}
