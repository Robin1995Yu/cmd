package com.zhuolu.cmd.ext.factory;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.factory.CmdFactory;
import com.zhuolu.cmd.ext.cmd.InvokeCmd;

import java.util.List;

public class InvokeCmdFactory implements CmdFactory {
    @Override
    public String getName() {
        return "invoke";
    }

    @Override
    public Cmd getCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        return new InvokeCmd(previous, param, cmdRuntime);
    }
}
