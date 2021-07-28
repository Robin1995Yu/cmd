package com.zhuolu.cmd.impl.cmd;

import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.entry.cmd.iterator.OnceIterator;
import com.zhuolu.cmd.core.CmdRuntime;

import java.util.Iterator;

public class QuitCmd extends AbstractCmd {
    public QuitCmd(Cmd previous, CmdRuntime cmdRuntime) {
        super("exit", previous, null, cmdRuntime);
    }

    @Override
    public void invoke() {
        getCmdRuntime().exit(0);
    }

    @Override
    protected Iterator<String> doIterator() {
        return new OnceIterator("Bye Bye");
    }
}
