package com.zhuolu.cmd.core.utils;

import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.factory.CmdFactory;

import java.util.Iterator;
import java.util.List;

/**
 * 命令相关的操作
 * @author zhuolu
 */
public interface CmdUtil {
    Iterator<String> invoke(String cmd);

    /**
     * 通过字符串获取命令对象
     * 这个方法会解析命令字符串 将命令头到第一个空格之间的字符串认为是命令名称
     * 将第一个空格之后的认为是参数
     * @param line 命令
     * @return 初始化完成的命令对象（已经初始化好了其链的关系）
     */
    Cmd initCmd(String line);

    /**
     * 获取单个命令
     * @param name 命令名称
     * @param previous 前一个命令
     * @param param 命令的参数
     * @return 单个命令
     */
    Cmd getCmd(String name, Cmd previous, List<String> param);

    /**
     * 获取命令的工厂
     * @param name 命令名
     * @return 对应的工厂
     * @see com.zhuolu.cmd.core.factory.CmdFactory
     */
    CmdFactory getCmdFactory(String name);
}
