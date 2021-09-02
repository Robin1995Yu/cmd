package com.zhuolu.cmd.impl.cmd;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.entry.cmd.iterator.BufferedReaderIterator;
import com.zhuolu.cmd.impl.domain.InvokeHolder;
import com.zhuolu.cmd.impl.factory.ResultCmdFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

public class SelectCmd extends AbstractCmd {
    private List<InvokeHolder> invokeHolderList;
    private int index;
    private InvokeHolder invokeHolder;
    private String result;

    public SelectCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime, List<InvokeHolder> invokeHolderList) {
        super("select", previous, param, cmdRuntime);
        this.invokeHolderList = invokeHolderList;
    }

    @Override
    public void invoke() {
        if (invokeHolderList == null || invokeHolderList.isEmpty()) {
            throw new IllegalArgumentException("do not has select context");
        }
        if (param.isEmpty()) {
            throw new IllegalArgumentException("select must has at least one param");
        }
        index = Integer.valueOf(param.get(0));
        try {
            invokeHolder = invokeHolderList.get(index);
        } catch (Throwable t) {
            throw new IllegalArgumentException("do not has such context");
        }
        try {
            ResultCmdFactory resultCmdFactory = (ResultCmdFactory) getCmdRuntime().getCmdUtil().getCmdFactory("result");
            result = invokeHolder.invoke(resultCmdFactory);
        } catch (Throwable t) {
            throw new IllegalArgumentException(t);
        }
    }

    @Override
    protected Iterator<String> doIterator() {
        return new BufferedReaderIterator(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8)))));
    }
}
