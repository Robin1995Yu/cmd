package com.zhuolu.cmd.cmdspringboot.autoconfigure;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.process.CmdStartProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextAddStartProcess implements CmdStartProcess {
    @Autowired
    private ApplicationContextExportContext exportContext;

    @Override
    public void process(CmdRuntime cmdRuntime) {
        cmdRuntime.getExportContextUtil().addExportContext(exportContext);
    }
}
