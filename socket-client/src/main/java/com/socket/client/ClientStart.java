package com.socket.client;

import com.socket.common.Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * socket client starter
 */
public class ClientStart {

    public static void main(String[] args) {
        try {
            new Client(new Socket("localhost", 8080)) {
                @Override
                public void exe() {
                    try {
                        Scanner scanner = new Scanner(System.in);
                        while (isConnected) {
                            // send
                            String line = scanner.nextLine();
                            if ("0".equals(line)) {
                                System.out.println("客户端退出!");
                                break;
                            }
                            dos.writeUTF(line);
                            dos.flush();
                            socket.shutdownOutput();

                            // read
                            String response = dis.readUTF();
                            System.out.println("SERVER : " + response);
                            socket.shutdownInput();
                        }
                        disconnect();
                    } catch (IOException e) {
                        disconnect();
                    }
                }
            }.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
