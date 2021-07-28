package com.zhuolu.cmd.core;

import com.zhuolu.cmd.core.utils.CmdUtil;
import com.zhuolu.cmd.core.utils.ExportContextUtil;
import com.zhuolu.cmd.core.utils.IOUtil;
import com.zhuolu.cmd.core.utils.PathUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public final class CmdRuntime implements AutoCloseable {
    private final ExportContextUtil exportContextUtil = new ExportContextUtil();
    private final IOUtil ioUtil;
    private final PathUtil pathUtil = new PathUtil();
    private final CmdUtil cmdUtil;
    private volatile boolean runFlag = true;

    private CmdRuntime(BufferedReader reader, BufferedWriter writer) {
        ioUtil = new IOUtil(reader, writer);
        cmdUtil = new CmdUtil(this);

    }

    public static CmdRuntime create(BufferedReader reader, BufferedWriter writer) {
        return new CmdRuntime(reader, writer);
    }

    public ExportContextUtil getExportContextUtil() {
        return exportContextUtil;
    }

    public IOUtil getIoUtil() {
        return ioUtil;
    }

    public PathUtil getPathUtil() {
        return pathUtil;
    }

    public void start(String[] args) {
        while (runFlag) {
            try {
                String line = ioUtil.reader().trim();
                if (line.length() != 0) {
                    cmdUtil.invoke(line);
                }
            } catch (Throwable e) {
                ioUtil.write(e.toString());
                ioUtil.newLine();
                ioUtil.flush();
            } finally {
                String name = pathUtil.pwd().getName();
                if (name.length() == 0) {
                    name = "/";
                }
                ioUtil.write("->\t" + name + ">>");
                ioUtil.flush();
            }
        }
    }

    public void exit(int status) {
        runFlag = false;
    }

    @Override
    public void close() throws Exception {
        ioUtil.close();
    }
}
