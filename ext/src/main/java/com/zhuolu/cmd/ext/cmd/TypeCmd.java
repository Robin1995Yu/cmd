package com.zhuolu.cmd.ext.cmd;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.entry.cmd.iterator.OnceIterator;

import java.util.Iterator;
import java.util.List;

public class TypeCmd extends AbstractCmd {
    public TypeCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        super("type", previous, param, cmdRuntime);
    }

    @Override
    protected void assertParam() {
        if (param == null || param.isEmpty()) {
            throw new IllegalArgumentException("type cmd must has one param");
        }
    }

    private Iterator<String> iterator;

    @Override
    public void invoke() {
        Object o = getCmdRuntime().getExportContextUtil().get(param.get(0));
        String name = o == null ? "null" : o.getClass().getName();
        iterator = new OnceIterator(name);
    }

    @Override
    protected Iterator<String> doIterator() {
        return iterator;
    }
}
