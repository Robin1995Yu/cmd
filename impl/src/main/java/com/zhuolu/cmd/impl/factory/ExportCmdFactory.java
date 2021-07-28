package com.zhuolu.cmd.impl.factory;

import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.factory.CmdFactory;
import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.impl.cmd.ExportCmd;

import java.util.List;

public class ExportCmdFactory implements CmdFactory {
    @Override
    public String getName() {
        return "export";
    }

    @Override
    public Cmd getCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        return new ExportCmd(previous, param, cmdRuntime);
    }
}
