package com.zhuolu.cmd.core.entry.export;

public interface AlterableExportContext extends ExportContext {
    void set(String name, Object value);
}
