package com.zhuolu.cmd.core.utils;

import java.io.File;

/**
 * 路径的工具类 表示当前运行的路径状态
 * @see com.zhuolu.cmd.core.CmdRuntime
 * @author zhuolu
 */
public interface PathUtil {
    /**
     * 获取当前的Path
     * @return 当前的Path的文件
     */
    File pwd();

    /**
     * 获取要切换到的路径所指代的File
     * 如果是<b> / </b>开头的 会认为是绝对路径
     * 如果非<b> / </b>开头的 会认为是相对路径 会去当前路径作为父路径
     * 如果是空字符串 会转到home
     * 如果是<b> .. </b>会到上级目录
     * 如果是<b> . </b>会到当前目录
     * @param path 要跳转的path字符串
     * @return 跳转后的File对象
     */
    File getPath(String path);

    /**
     * 切换路径
     * @param path 路径名称
     */
    void cd(String path);
}
