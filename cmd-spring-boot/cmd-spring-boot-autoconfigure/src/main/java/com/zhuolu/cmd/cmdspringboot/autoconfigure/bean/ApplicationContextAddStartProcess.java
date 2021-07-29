package com.zhuolu.cmd.cmdspringboot.autoconfigure.bean;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.process.CmdStartProcess;

public class ApplicationContextAddStartProcess implements CmdStartProcess {
    private ApplicationContextExportContext exportContext;

    public void setExportContext(ApplicationContextExportContext exportContext) {
        this.exportContext = exportContext;
    }

    @Override
    public void process(CmdRuntime cmdRuntime) {
        cmdRuntime.getExportContextUtil().addExportContext(exportContext);
    }
}
