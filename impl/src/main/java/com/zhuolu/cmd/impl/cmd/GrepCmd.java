package com.zhuolu.cmd.impl.cmd;

import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.CmdRuntime;

import java.util.Iterator;
import java.util.List;

public class GrepCmd extends AbstractCmd {
    public GrepCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        super("grep", previous, param, cmdRuntime);
    }

    @Override
    protected void init() {
        if (param.isEmpty()) {
            throw new IllegalArgumentException("command \"grep\" must has at lest one param");
        }
    }

    @Override
    protected boolean assertNext(String nextLine) {
        return nextLine.indexOf(param.get(0)) >= 0;
    }

    @Override
    protected Iterator<String> doIterator() {
        return new CmdIterator();
    }
}
