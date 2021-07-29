package com.zhuolu.cmd.starter.autoconfigure;

import com.zhuolu.cmd.core.entry.export.ExportContext;
import org.springframework.context.ApplicationContext;

public class ApplicationContextExportContext implements ExportContext {
    private final ApplicationContext applicationContext;

    public ApplicationContextExportContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Object get(String name) {
        return applicationContext.getBean(name);
    }
}
