package com.zhuolu.cmd.core;

import com.zhuolu.cmd.core.entry.process.CmdStartProcess;
import com.zhuolu.cmd.core.utils.CmdUtil;
import com.zhuolu.cmd.core.utils.ExportContextUtil;
import com.zhuolu.cmd.core.utils.PathUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class CmdRuntime implements Comparable<CmdRuntime> {
    private static AtomicInteger currId = new AtomicInteger(1);
    private final long id;

    private static long getId() {
        int currCount = currId.getAndIncrement();
        long currSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        return (currSecond << 32) + currCount;
    }

    private final ExportContextUtil exportContextUtil = new ExportContextUtil();
    private final PathUtil pathUtil = new PathUtil();
    private final CmdUtil cmdUtil;
    private volatile boolean runFlag = true;

    private CmdRuntime() {
        id = getId();
        cmdUtil = new CmdUtil(this);

    }

    public static CmdRuntime create(List<CmdStartProcess> processes) {
        CmdRuntime runtime = new CmdRuntime();
        for (CmdStartProcess process : processes) {
            process.process(runtime);
        }
        return runtime;
    }

    public ExportContextUtil getExportContextUtil() {
        return exportContextUtil;
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

    public void exit(int i) {
        runFlag = false;
    }

    public ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }

    @Override
    public int compareTo(CmdRuntime other) {
        return Long.compare(this.id, other.id);
    }
}
