package com.zhuolu.cmd.test.util;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CmdMock extends AbstractCmd {
    private List<String> resultList;

    public CmdMock( CmdRuntime cmdRuntime, String ... results) {
        super(null, null, null, cmdRuntime);
        this.resultList = Arrays.asList(results);
    }

    @Override
    protected Iterator<String> doIterator() {
        return resultList.iterator();
    }
}
