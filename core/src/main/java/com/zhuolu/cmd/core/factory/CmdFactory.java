package com.zhuolu.cmd.core.factory;

import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.CmdRuntime;

import java.util.List;

/**
 * 命令工厂
 * @author zhuolu
 */
public interface CmdFactory {
    /**
     * 命令名 这个名字必须全局唯一
     * @return 当前工厂对应的命令名
     */
    String getName();

    /**
     * 获取命令的工厂方法
     * @param previous 这个命令的上一个命令 如果当前命令是第一个 则为null
     * @param param 命令的参数
     * @param cmdRuntime 当前命令所对应的CmdRuntime
     * @return 命令
     * @see com.zhuolu.cmd.core.CmdRuntime
     */
    Cmd getCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime);
}
