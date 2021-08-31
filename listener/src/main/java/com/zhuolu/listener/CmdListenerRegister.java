package com.zhuolu.listener;

import com.zhuolu.cmd.core.CmdRuntime;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CmdListenerRegister {
    private static final Map<CmdRuntime, CmdListenerRegister> CMD_LISTENER_REGISTER_MAP = new HashMap<>();

    public static CmdListenerRegister getCmdListenerRegister(CmdRuntime cmdRuntime) {
        if (cmdRuntime == null) {
            throw new IllegalArgumentException("null");
        }
        if (CMD_LISTENER_REGISTER_MAP.get(cmdRuntime) == null) {
            synchronized (CMD_LISTENER_REGISTER_MAP) {
                if (CMD_LISTENER_REGISTER_MAP.get(cmdRuntime) == null) {
                    CmdListenerRegister cmdListenerRegister = new CmdListenerRegister();
                    CMD_LISTENER_REGISTER_MAP.put(cmdRuntime, cmdListenerRegister);
                }
            }
        }
        return CMD_LISTENER_REGISTER_MAP.get(cmdRuntime);
    }


}
