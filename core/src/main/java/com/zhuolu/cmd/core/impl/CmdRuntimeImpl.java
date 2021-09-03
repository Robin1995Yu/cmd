package com.zhuolu.cmd.core.impl;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.utils.CmdUtil;
import com.zhuolu.cmd.core.utils.ExportContextUtil;
import com.zhuolu.cmd.core.utils.PathUtil;
import com.zhuolu.cmd.core.utils.impl.CmdUtilImpl;
import com.zhuolu.cmd.core.utils.impl.ExportContextUtilImpl;
import com.zhuolu.cmd.core.utils.impl.PathUtilImpl;

public final class CmdRuntimeImpl implements CmdRuntime {
    private final ExportContextUtil exportContextUtil = new ExportContextUtilImpl();
    private final PathUtil pathUtil = new PathUtilImpl();
    private final CmdUtil cmdUtil;
    private volatile boolean runFlag = true;

    public CmdRuntimeImpl() {
        cmdUtil = new CmdUtilImpl(this);
    }

    @Override
    public ExportContextUtil getExportContextUtil() {
        return exportContextUtil;
    }

    @Override
    public PathUtil getPathUtil() {
        return pathUtil;
    }

    @Override
    public CmdUtil getCmdUtil() {
        return cmdUtil;
    }

    @Override
    public boolean isRunFlag() {
        return runFlag;
    }

    @Override
    public void exit(int i) {
        runFlag = false;
    }

    public ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }
}
