package com.zhuolu.cmd.impl.factory;

import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.factory.CmdFactory;
import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.impl.cmd.CdCmd;

import java.util.List;

public class CdCmdFactory implements CmdFactory {
    @Override
    public String getName() {
        return "cd";
    }

    @Override
    public Cmd getCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        return new CdCmd(previous, param, cmdRuntime);
    }
}
