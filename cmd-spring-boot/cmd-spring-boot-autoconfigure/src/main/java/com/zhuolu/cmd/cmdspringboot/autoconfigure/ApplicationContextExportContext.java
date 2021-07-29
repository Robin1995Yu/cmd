package com.zhuolu.cmd.cmdspringboot.autoconfigure;

import com.zhuolu.cmd.core.entry.export.ExportContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextExportContext implements ExportContext, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public Object get(String name) {
        return applicationContext.getBean(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
