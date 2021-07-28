package com.zhuolu.cmd.core.entry.export.impl;

import com.zhuolu.cmd.core.entry.export.ExportContext;

import java.util.Properties;

public class PropertiesExportContext implements ExportContext {
    public PropertiesExportContext(Properties properties) {
        this.properties = properties;
    }

    private Properties properties;

    @Override
    public Object get(String name) {
        return properties.get(name);
    }
}
