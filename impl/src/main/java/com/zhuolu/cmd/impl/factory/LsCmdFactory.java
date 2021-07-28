package com.zhuolu.cmd.impl.factory;

import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.factory.CmdFactory;
import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.impl.cmd.LsCmd;

import java.util.List;

public class LsCmdFactory implements CmdFactory {
    @Override
    public String getName() {
        return "ls";
    }

    @Override
    public Cmd getCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        return new LsCmd(previous, param, cmdRuntime);
    }
}
