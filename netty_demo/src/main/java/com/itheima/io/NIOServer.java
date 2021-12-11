package com.itheima.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 负责轮询是否有新的连接
        Selector serverSelector = Selector.open();
        // 负责轮询处理连接中的数据
        Selector clientSelector = Selector.open();

        new Thread() {
            @Override
            public void run() {
                try {
                    // 对应IO编程中服务端启动
                    ServerSocketChannel listenerChannel = ServerSocketChannel.open();
                    listenerChannel.socket().bind(new InetSocketAddress(8000));
                    listenerChannel.configureBlocking(false);
                    // OP_ACCEPT表示服务器监听到了客户连接，服务器可以接收这个连接了
                    listenerChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

                    while (true) {
                        // 监测是否有新的连接，这里的1指的是阻塞的时间为1ms
                        if (serverSelector.select(1) > 0) {
                            Set<SelectionKey> set = serverSelector.selectedKeys();
                            Iterator<SelectionKey> keyIterator = set.iterator();

                            while (keyIterator.hasNext()) {
                                SelectionKey key = keyIterator.next();

                                if (key.isAcceptable()) {
                                    try {
                                        // (1) 每来一个新连接，不需要创建一个线程，而是直接注册到clientSelector
                                        SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
                                        clientChannel.configureBlocking(false);
                                        // OP_READ表示通道中已经有了可读的数据，可以执行读操作了（通道目前有数据，可以进行读操作了）
                                        clientChannel.register(clientSelector, SelectionKey.OP_READ);
                                    } finally {
                                        keyIterator.remove();
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException ignored) {
                }
            }
        }.start();


        new Thread() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                try {
                    while (true) {
                        // (2) 批量轮询是否有哪些连接有数据可读，这里的1指的是阻塞的时间为1ms
                        if (clientSelector.select(1) > 0) {
                            Set<SelectionKey> set = clientSelector.selectedKeys();
                            Iterator<SelectionKey> keyIterator = set.iterator();

                            while (keyIterator.hasNext()) {
                                SelectionKey key = keyIterator.next();

                                if (key.isReadable()) {
                                    try {
                                        SocketChannel clientChannel = (SocketChannel) key.channel();
                                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                        // (3) 读取数据以块为单位批量读取
                                        clientChannel.read(byteBuffer);
                                        byteBuffer.flip();
                                        System.out.println("线程" + name + ":" + Charset.defaultCharset().newDecoder().decode(byteBuffer)
                                                .toString());
                                    } finally {
                                        keyIterator.remove();
                                        key.interestOps(SelectionKey.OP_READ);
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException ignored) {
                }
            }
        }.start();
    }
}