package com.zhuolu.cmd.core.factory;

import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.CmdRuntime;

import java.util.List;

public interface CmdFactory {
    String getName();

    Cmd getCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime);
}
