package com.itheima.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-08 17:21
 * @Description:
 */
public class IOServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8000);
        while (true) {
            Socket socket = serverSocket.accept();
            // 每个client链接都会创建一个新线程
            new Thread() {
                @Override
                public void run() {
                    String name = Thread.currentThread().getName();
                    try {
                        byte[] buffer = new byte[1024];
                        InputStream is = socket.getInputStream();
                        while (true) {
                            int len;
                            while ((len = is.read(buffer)) != -1) {
                                System.out.println("线程：" + name + ":" + new String(buffer,0, len ));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
