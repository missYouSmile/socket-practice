package com.socket.server;

import com.socket.common.Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class OnLineClient extends Client {

    private long clientId;

    public OnLineClient(long clientId, Socket socket) {
        super(socket);
        this.clientId = clientId;
    }

    @Override
    public void exe() {
        try {
            Scanner scanner = new Scanner(System.in);
            while (isConnected) {
                // read
                String request = dis.readUTF();
                System.out.println("CLIENT : " + request);

                // response
                String line = scanner.nextLine();
                if ("0".equals(line)) {
                    System.out.println("服务端退出! 客户端id -> " + getClientId());
                    break;
                }
                dos.writeUTF(line);
                dos.flush();
            }
        } catch (IOException e) {
            disconnect();
        }
    }

    public long getClientId() {
        return this.clientId;
    }
}
