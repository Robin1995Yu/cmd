package com.zhuolu.cmd.cmdspringboot.autoconfigure.bean;

import com.zhuolu.cmd.core.entry.export.ExportContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextExportContext implements ExportContext, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public Object get(String name) {
        try {
            return applicationContext.getBean(name);
        } catch (Throwable t) {
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
