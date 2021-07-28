package com.zhuolu.cmd.impl.cmd;

import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.CmdRuntime;

import java.util.List;

public class ExportCmd extends AbstractCmd {
    public ExportCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        super("export", previous, param, cmdRuntime);
    }

    @Override
    protected void assertParam() {
        if (param.size() < 2) {
            throw new IllegalArgumentException("export' param length must be 2");
        }
    }

    @Override
    public void invoke() {
        getCmdRuntime().getExportContextUtil().set(param.get(0), param.get(1));
    }
}
