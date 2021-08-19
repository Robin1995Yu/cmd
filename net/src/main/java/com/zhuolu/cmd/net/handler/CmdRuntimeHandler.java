package com.zhuolu.cmd.net.handler;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.net.io.ReplaceableInputStream;
import com.zhuolu.cmd.net.io.ReplaceableOutputStream;

import java.io.InputStream;
import java.io.OutputStream;

public class CmdRuntimeHandler {
    private final CmdRuntime cmdRuntime;
    private final ReplaceableInputStream inputStream;
    private final ReplaceableOutputStream outputStream;

    public CmdRuntimeHandler(CmdRuntime cmdRuntime, ReplaceableInputStream inputStream, ReplaceableOutputStream outputStream) {
        this.cmdRuntime = cmdRuntime;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public CmdRuntime getCmdRuntime() {
        return cmdRuntime;
    }

    public ReplaceableInputStream getInputStream() {
        return inputStream;
    }

    public ReplaceableOutputStream getOutputStream() {
        return outputStream;
    }
}
