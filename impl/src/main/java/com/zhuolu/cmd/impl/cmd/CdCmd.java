package com.zhuolu.cmd.impl.cmd;

import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.CmdRuntime;

import java.util.List;

public class CdCmd extends AbstractCmd {
    public CdCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        super("cd", previous, param, cmdRuntime);
    }

    @Override
    protected void init() {
        if (param.size() >= 2) {
            throw new IllegalArgumentException("cd param length bigger then 1");
        }
    }

    @Override
    public void invoke() {
        String path = "";
        if (!param.isEmpty()) {
            path = param.get(0);
        }
        getCmdRuntime().getPathUtil().cd(path);
    }
}
