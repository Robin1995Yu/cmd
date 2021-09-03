package com.zhuolu.cmd.core.entry.export;

/**
 * 可变更的
 * @author zhuolu
 */
public interface AlterableExportContext extends ExportContext {
    /**
     * 设置参数
     * @param name 参数名
     * @param value 参数
     */
    void set(String name, Object value);
}
