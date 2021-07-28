package com.zhuolu.cmd.core.entry.cmd;


import com.zhuolu.cmd.core.CmdRuntime;

public interface Cmd extends Iterable<String> {
    String getName();

    void invoke();

    default void destroy() {}

    CmdRuntime getCmdRuntime();
}
