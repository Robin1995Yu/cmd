package com.zhuolu.cmd.test;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.utils.CmdUtil;
import com.zhuolu.cmd.core.utils.ExportContextUtil;
import com.zhuolu.cmd.core.utils.PathUtil;
import com.zhuolu.cmd.core.utils.impl.ExportContextUtilImpl;
import com.zhuolu.cmd.test.util.PathUtilMock;

public class CmdRuntimeMock implements CmdRuntime {
    private final ExportContextUtil exportContextUtil = new ExportContextUtilImpl();
    private final PathUtil pathUtil = new PathUtilMock();
    private volatile boolean runFlag = true;

    public CmdRuntimeMock() {
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
        throw new UnsupportedOperationException();
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
