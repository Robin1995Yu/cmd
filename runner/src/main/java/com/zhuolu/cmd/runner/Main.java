package com.zhuolu.cmd.runner;

import com.zhuolu.cmd.net.socket.CmdSocketServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        CmdSocketServer.run(args);
    }
}
