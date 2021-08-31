package com.zhuolu.cmd.core;

import com.zhuolu.cmd.core.entry.process.CmdStartProcess;
import com.zhuolu.cmd.core.utils.CmdUtil;
import com.zhuolu.cmd.core.utils.ExportContextUtil;
import com.zhuolu.cmd.core.utils.IOUtil;
import com.zhuolu.cmd.core.utils.PathUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class CmdRuntime implements AutoCloseable, Comparable<CmdRuntime> {
    private static AtomicInteger currId = new AtomicInteger(1);
    private final long id;

    private static long getId() {
        int currCount = currId.getAndIncrement();
        long currSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        return (currSecond << 32) + currCount;
    }

    private final ExportContextUtil exportContextUtil = new ExportContextUtil();
    private final IOUtil ioUtil;
    private final PathUtil pathUtil = new PathUtil();
    private final CmdUtil cmdUtil;
    private volatile boolean runFlag = true;

    private CmdRuntime(BufferedReader reader, BufferedWriter writer) {
        id = getId();
        ioUtil = new IOUtil(reader, writer);
        cmdUtil = new CmdUtil(this);

    }

    public static CmdRuntime create(BufferedReader reader, BufferedWriter writer, List<CmdStartProcess> processes) {
        CmdRuntime runtime = new CmdRuntime(reader, writer);
        for (CmdStartProcess process : processes) {
            process.process(runtime);
        }
        return runtime;
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

    public CmdUtil getCmdUtil() {
        return cmdUtil;
    }

    public boolean isRunFlag() {
        return runFlag;
    }

    public void start() {
        start(Collections.emptyList());
    }

    public void start(List<CmdStartProcess> processes) {
        for (CmdStartProcess process : processes) {
            process.process(this);
        }
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

    public ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }

    @Override
    public int compareTo(CmdRuntime other) {
        return Long.compare(this.id, other.id);
    }
}
