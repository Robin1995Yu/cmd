package com.zhuolu.cmd.ext.factory;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.factory.CmdFactory;
import com.zhuolu.cmd.ext.cmd.TypeCmd;

import java.util.List;

public class TypeCmdFactory implements CmdFactory {
    @Override
    public String getName() {
        return "type";
    }

    @Override
    public Cmd getCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        return new TypeCmd(previous, param, cmdRuntime);
    }
}
