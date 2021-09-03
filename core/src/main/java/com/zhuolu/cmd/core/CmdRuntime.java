package com.zhuolu.cmd.core;

import com.zhuolu.cmd.core.entry.process.CmdStartProcess;
import com.zhuolu.cmd.core.impl.CmdRuntimeImpl;
import com.zhuolu.cmd.core.utils.CmdUtil;
import com.zhuolu.cmd.core.utils.ExportContextUtil;
import com.zhuolu.cmd.core.utils.PathUtil;

import java.util.List;

/**
 * Cmd运行时上下文 每一个Cmd session会包含一个唯一的CmdRuntime
 * @author zhuolu
 */
public interface CmdRuntime {

    /**
     * 默认实现的创建
     * @param processes 前置任务
     * @return
     */
    static CmdRuntime create(List<CmdStartProcess> processes) {
        CmdRuntime runtime = new CmdRuntimeImpl();
        for (CmdStartProcess process : processes) {
            process.process(runtime);
        }
        return runtime;
    }

    /**
     * 返回参数的上下文
     * @return
     * @see com.zhuolu.cmd.core.utils.ExportContextUtil
     */
    ExportContextUtil getExportContextUtil();

    /**
     * 返回路径上下文
     * @return
     * @see com.zhuolu.cmd.core.utils.PathUtil
     */
    PathUtil getPathUtil();

    /**
     * 返回命令上下文
     * @return
     * @see com.zhuolu.cmd.core.utils.CmdUtil
     */
    CmdUtil getCmdUtil();

    /**
     * 当前CmdRuntime是否还在运行
     * 不会影响CmdRuntime的正常功能 用户可以通过判断这个状态来判断是否结束回话
     * @return
     */
    boolean isRunFlag();

    /**
     * 退出 在调用这个方法后 isRunFlag用于返回false
     * @param i
     */
    void exit(int i);
}
