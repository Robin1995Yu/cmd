package com.zhuolu.cmd.net.handler;

import com.zhuolu.cmd.core.CmdRuntime;

import java.io.InputStream;
import java.io.OutputStream;

public class CmdRuntimeHandler {
    private final CmdRuntime cmdRuntime;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public CmdRuntimeHandler(CmdRuntime cmdRuntime, InputStream inputStream, OutputStream outputStream) {
        this.cmdRuntime = cmdRuntime;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public CmdRuntime getCmdRuntime() {
        return cmdRuntime;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
