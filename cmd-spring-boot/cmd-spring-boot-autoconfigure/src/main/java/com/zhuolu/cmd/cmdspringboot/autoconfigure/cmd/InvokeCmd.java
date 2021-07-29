package com.zhuolu.cmd.cmdspringboot.autoconfigure.cmd;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;

import java.util.List;

public class InvokeCmd extends AbstractCmd {
    public InvokeCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        super("invoke", previous, param, cmdRuntime);
    }

    @Override
    protected void assertParam() {
        if (param == null || param.isEmpty()) {
            throw new IllegalArgumentException("invoke cmd must has one more param");
        }
    }
}
