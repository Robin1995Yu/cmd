package com.zhuolu.cmd.core.entry.export.impl;

import com.zhuolu.cmd.core.entry.export.AlterableExportContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AlterableExportContextImpl implements AlterableExportContext {
    private final Map<String, Object> context = new ConcurrentHashMap<>();

    @Override
    public void set(String name, Object value) {
        context.put(name, value);
    }

    @Override
    public Object get(String name) {
        return context.get(name);
    }
}
