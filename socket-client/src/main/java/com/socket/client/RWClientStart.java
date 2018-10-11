package com.socket.client;

import java.io.IOException;
import java.net.Socket;

import com.socket.common.ReadAndWriteClient;

/**
 * @author shiwen.chen
 * @date 2018-10-11 11:17
 */
public class RWClientStart {

    public static void main(String[] args) throws IOException {
        new Thread(new ReadAndWriteClient(new Socket("localhost", 8080))).start();
    }

}
