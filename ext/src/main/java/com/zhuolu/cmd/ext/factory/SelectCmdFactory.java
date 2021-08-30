package com.zhuolu.cmd.ext.factory;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.factory.CmdFactory;
import com.zhuolu.cmd.ext.cmd.SelectCmd;
import com.zhuolu.cmd.ext.domain.InvokeHolder;

import java.util.ArrayList;
import java.util.List;

public class SelectCmdFactory implements CmdFactory {
    private ArrayList<InvokeHolder> invokeHolderList = new ArrayList<>();

    @Override
    public String getName() {
        return "select";
    }

    @Override
    public Cmd getCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        return new SelectCmd(previous, param, cmdRuntime, invokeHolderList);
    }

    public void resetInvokeHandlerList(List<InvokeHolder> invokeHolderList) {
        if (invokeHolderList == null && invokeHolderList.isEmpty()) {
            throw new IllegalArgumentException("invoke holder can not be empty");
        }
        this.invokeHolderList.clear();
        this.invokeHolderList.ensureCapacity(invokeHolderList.size());
        for (InvokeHolder invokeHolder : invokeHolderList) {
            this.invokeHolderList.add(invokeHolder);
        }
    }
}
