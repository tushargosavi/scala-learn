package com.tugo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AsyncServer {
    ServerSocketChannel serverChannel;
    int port;
    int totalAccepted = 0;
    Selector selector;
    Map<SelectionKey, ByteBuffer> buffers = new HashMap<>();

    // this has to be the server socket channel
    void acceptConnection(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        // wait for read on the socket
        SelectionKey ckey = sc.register(selector, SelectionKey.OP_READ);
        buffers.put(ckey, ByteBuffer.allocate(1024));
    }

    void handleRead(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel)key.channel();
        if (!sc.isConnected()) {
            buffers.remove(key);
            return;
        }

        ByteBuffer bb = buffers.get(key);
        int len = sc.read(bb);
        if (len <= 0) {
            // socket is closed
            sc.close();
            buffers.remove(key);
            return;
        }
        System.out.println("read " + len + " bytes from socket " + bb);
        bb.flip();
        transform(bb);
        key.interestOps(SelectionKey.OP_WRITE);
    }

    public void transform(ByteBuffer bb) {
        for(int i = bb.position(); i < bb.limit(); i++) {
            int b = bb.get(i);
            if (Character.isLetter(b)) {
                b = b ^ ' ';
            }
            bb.put(i, (byte)b);
        }
    }

    void handleWrite(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer bb = buffers.get(key);
        System.out.println("write buffer is " + bb);
        int ret = sc.write(bb);
        System.out.println("write call returned " + ret);
        // if can't write all the data, wait for write again
        if (bb.hasRemaining()) {
           bb.compact();
           key.interestOps(SelectionKey.OP_WRITE);
       } else {
           // if all data is written, switch back to the read mode
           key.interestOps(SelectionKey.OP_READ);
      }
    }

    void loop() throws IOException {
        while (true) {
            int ret = selector.select();
            if (ret <= 0) continue;
            System.out.println("number of active channels " + ret);
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iter = keys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    acceptConnection(key);
                } else if (key.isReadable()) {
                    System.out.println("socket ready for read");
                    handleRead(key);
                } else if (key.isWritable()) {
                    System.out.println("socket ready for write");
                    handleWrite(key);
                }
                // remote the key, to avoid sending the message again
            }
        }
    }

    void start(int port) throws IOException {
        this.port = port;
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        selector = Selector.open();
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("started server on port " + port);
        loop();
    }

    public static void main(String[] args) throws IOException {
        AsyncServer server = new AsyncServer();
        server.start(8082);
    }
}
