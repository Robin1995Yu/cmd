package com.zhuolu.cmd.ext.cmd;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;

import java.util.List;

public class InvokeCmd extends AbstractCmd {
    public InvokeCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        super("invoke", previous, param, cmdRuntime);
    }
}
