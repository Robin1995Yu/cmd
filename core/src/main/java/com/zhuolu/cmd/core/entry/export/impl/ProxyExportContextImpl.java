package com.zhuolu.cmd.core.entry.export.impl;

import com.zhuolu.cmd.core.entry.export.ExportContext;
import com.zhuolu.cmd.core.entry.export.ProxyExportContext;

import java.util.ArrayList;
import java.util.List;

public class ProxyExportContextImpl implements ProxyExportContext {
    private List<ExportContext> contexts;

    public ProxyExportContextImpl() {
        this.contexts = new ArrayList<>();
    }

    public ProxyExportContextImpl(List<ExportContext> exportContexts) {
        this.contexts = new ArrayList<>(exportContexts);
    }

    @Override
    public Object get(String name) {
        for (ExportContext context : contexts) {
            Object t = context.get(name);
            if (t != null) {
                return t;
            }
        }
        return null;
    }

    @Override
    public void addExportContext(ExportContext exportContext) {
        contexts.add(exportContext);
    }
}
