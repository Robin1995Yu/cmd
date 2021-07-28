package com.zhuolu.cmd.starter.autoconfigure;

import com.zhuolu.cmd.core.entry.export.ExportContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CmdApplicationListener implements ApplicationListener<CmdStartApplicationEvent>, ApplicationContextAware {

    private static ExportContext exportContext;

    @Override
    @Async
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (exportContext == null) {
            synchronized (this) {
                if (exportContext == null) {
                    exportContext = new ApplicationContextExportContext(applicationContext);
                }
            }
        }
    }

    @Override
    public void onApplicationEvent(CmdStartApplicationEvent event) {
//        CmdRuntime.getRuntime().addExportContext(exportContext);
    }
}
