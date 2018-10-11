package com.socket.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author shiwen.chen
 * @date 2018-10-11 10:58
 */
public class ReadAndWriteClient implements Runnable {

    private Thread read;
    private Thread write;

    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean isConnected;
    private Socket socket;

    public ReadAndWriteClient(Socket socket) {
        Objects.requireNonNull(socket);
        try {
            this.socket = socket;
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            isConnected = true;
        } catch (IOException e) {
            disconnect();
            System.err.println("初始化失败,聊天退出...");
        }
    }

    @Override
    public void run() {
        read = new Thread(this::read);
        write = new Thread(this::write);
        read.start();
        write.start();
    }

    private void read() {
        try {
            while (isConnected) {
                String result = dis.readUTF();
                System.out.println("读取结果 : " + result);
            }
        } catch (IOException e) {
            if (isConnected) {
                System.err.println("读取数据失败,断开连接...");
                disconnect();
            }
        }
    }

    private void write() {
        try {
            System.out.println("请输入数据 : ");
            while (isConnected) {
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                if ("0".equals(line)) {
                    System.out.println("你选择了结束通信...");
                    break;
                }
                dos.writeUTF(line);
                dos.flush();
            }
            disconnect();
        } catch (IOException e) {
            System.err.println("程序出错了,断开连接...");
            disconnect();
        }
    }

    private void disconnect() {
        try {
            if (dis != null)
                dis.close();
            if (dos != null)
                dos.close();
            if (socket != null)
                socket.close();

            isConnected = false;
        } catch (IOException e) {
            System.err.println("连接断开异常...");
        }
    }
}
