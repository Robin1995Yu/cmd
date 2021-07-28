package com.zhuolu.cmd.net.socket;

import com.zhuolu.cmd.core.CmdRuntime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CmdSocketServer {
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            10,
            20,
            30,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(1)
    );

    public static void run(String[] args) throws IOException {
        int port = 5200;
        if (args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        ServerSocket ss = new ServerSocket(port);
        while (true) {
            Socket socket = ss.accept();
            System.out.println(socket + " open");
            executor.execute(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    CmdRuntime cmdRuntime = CmdRuntime.create(reader, writer);
                    cmdRuntime.start(args);
                    System.out.println(socket + " close");
                    cmdRuntime.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
