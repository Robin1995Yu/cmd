package com.zhuolu.cmd.core.entry.export;

/**
 * 参数 可以通过<b> ${exportName} </b>获取参数
 * @author zhuolu
 */
public interface ExportContext {
    /**
     * 获取参数
     * @param name 参数名
     * @return 得到的参数 如果不存在 则返回null
     */
    Object get(String name);
}
