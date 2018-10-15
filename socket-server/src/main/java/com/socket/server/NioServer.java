package com.socket.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.socket.common.log.Log;

/**
 * @author shiwen.chen
 * @date 2018-10-15 10:30
 */
public class NioServer implements Runnable {

    private static final int PORT = 8080;

    private static final int BUFFER_SIZE = 1024;

    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer;

    private Selector selector;

    public NioServer() {
        try {

            ServerSocketChannel socketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            socketChannel.bind(new InetSocketAddress(PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);

            readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        } catch (IOException e) {
            Log.error("server init failed!");
        }
    }


    @Override
    public void run() {
        Log.info("the listen port is : {}", PORT);
        while (true) {
            try {
                int count = selector.select();
                Log.info("请求个数:{}", count);
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    Log.info("ready options is : {} , and is valid is : {}", key.readyOps(), key.isValid());
                    // receive request
                    if (key.isValid() && key.isAcceptable()) {
                        Log.info("接收到客户端请求...");
                        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                        SocketChannel clientChannel = serverChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                    }
                    // read input
                    if (key.isValid() && key.isReadable()) {
                        Log.info("读取客户端请求...");
                        SocketChannel channel = (SocketChannel) key.channel();

                        readBuffer.clear();

                        int len;
                        StringBuilder builder = new StringBuilder();
                        while ((len = channel.read(readBuffer)) > 0) {
                            readBuffer.flip(); // readable
                            String temp = new String(readBuffer.array(), 0, len);
                            builder.append(temp);
                        }

                        Log.info("客户端请求数据 : {}", builder);

                        channel.register(selector, SelectionKey.OP_WRITE);

                        writeBuffer.clear();
                        writeBuffer.put("thanks for request!".getBytes());

                        if (len < 0) {
                            channel.close();
                        }
                    }
                    // reply
                    if (key.isValid() && key.isWritable()) {
                        Log.info("响应客户端...");
                        SocketChannel channel = (SocketChannel) key.channel();
                        writeBuffer.flip();
                        channel.write(writeBuffer);
                    }
                }
            } catch (IOException e) {
                Log.error("请求处理异常: {}", e);
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new NioServer()).start();
    }

}
