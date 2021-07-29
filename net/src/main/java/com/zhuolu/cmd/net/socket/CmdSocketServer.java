package com.zhuolu.cmd.net.socket;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.process.CmdStartProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CmdSocketServer {
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(
            10,
            20,
            30,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(1)
    );

    private List<CmdStartProcess> processes;

    public void setProcesses(List<CmdStartProcess> processes) {
        this.processes = processes;
    }

    public void run(int port) {
        executor.execute(() -> {
            try (ServerSocket ss = new ServerSocket(port)) {
                while (true) {
                    Socket socket = ss.accept();
                    System.out.println(socket + " open");
                    executor.execute(() -> {
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            CmdRuntime cmdRuntime = CmdRuntime.create(reader, writer);
                            List<CmdStartProcess> processes = Collections.emptyList();
                            if (this.processes != null) {
                                processes = this.processes;
                            }
                            cmdRuntime.start(processes);
                            System.out.println(socket + " close");
                            cmdRuntime.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
