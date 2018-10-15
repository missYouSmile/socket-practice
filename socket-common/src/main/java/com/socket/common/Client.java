package com.socket.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

/**
 * 读,写 应该使用不同线程
 */
public abstract class Client extends Thread {

    protected DataInputStream dis;
    protected DataOutputStream dos;
    protected boolean isConnected;
    protected Socket socket;

    public Client(Socket socket) {
        Objects.requireNonNull(socket);
        IOException ex = null;
        try {
            this.socket = socket;
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            isConnected = true;
        } catch (IOException e) {
            ex = e;
        } finally {
            if (ex != null) {
                disconnect();
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void run() {
        exe();
    }

    public abstract void exe();

    public void disconnect() {
        try {
            if (dis != null)
                dis.close();
            if (dos != null)
                dos.close();
            if (socket != null)
                socket.close();

            isConnected = false;
        } catch (IOException e) {
            // ignore
        }
    }

}
