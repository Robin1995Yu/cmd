package com.zhuolu.cmd.impl.cmd;

import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.entry.cmd.iterator.OnceIterator;
import com.zhuolu.cmd.core.CmdRuntime;

import java.util.Iterator;

public class PwdCmd extends AbstractCmd {
    public PwdCmd(Cmd previous, CmdRuntime cmdRuntime) {
        super("pwd", previous, null, cmdRuntime);
    }

    @Override
    protected Iterator<String> doIterator() {
        return new OnceIterator(getCmdRuntime().getPathUtil().pwd().getAbsolutePath());
    }
}
