package com.zhuolu.cmd.core.utils;

import com.zhuolu.cmd.core.entry.export.AlterableExportContext;
import com.zhuolu.cmd.core.entry.export.ExportContext;
import com.zhuolu.cmd.core.entry.export.ProxyExportContext;
import com.zhuolu.cmd.core.entry.export.impl.AlterableExportContextImpl;
import com.zhuolu.cmd.core.entry.export.impl.PropertiesExportContext;
import com.zhuolu.cmd.core.entry.export.impl.ProxyExportContextImpl;

import java.util.ArrayList;
import java.util.List;

public final class ExportContextUtil implements AlterableExportContext, ProxyExportContext {
    public ExportContextUtil() {
        ExportContext propertiesExportContext = new PropertiesExportContext(System.getProperties());
        List<ExportContext> exportContexts = new ArrayList<>(3);
        exportContexts.add(alterableExportContext);
        exportContexts.add(extExportContext);
        exportContexts.add(propertiesExportContext);
        exportContext = new ProxyExportContextImpl(exportContexts);
    }

    private final ProxyExportContext extExportContext = new ProxyExportContextImpl();

    private final AlterableExportContext alterableExportContext = new AlterableExportContextImpl();

    private final ExportContext exportContext;

    @Override
    public Object get(String name) {
        return exportContext.get(name);
    }

    @Override
    public void set(String name, Object value) {
        alterableExportContext.set(name, value);
    }

    @Override
    public void addExportContext(ExportContext exportContext) {
        extExportContext.addExportContext(exportContext);
    }
}
