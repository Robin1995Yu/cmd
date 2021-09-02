package com.zhuolu.cmd.impl.factory;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.factory.CmdFactory;
import com.zhuolu.cmd.impl.cmd.ResultCmd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ResultCmdFactory implements CmdFactory {
    private Map<String, Object> resultMap = new HashMap<>();
    private AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public String getName() {
        return "result";
    }

    @Override
    public Cmd getCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        return new ResultCmd(previous, param, cmdRuntime);
    }

    public void addResult(String key, Object result) {
        resultMap.put(key, result);
    }

    public Object getResult(String key) {
        return resultMap.get(key);
    }

    public Object delResult(String key) {
        return resultMap.remove(key);
    }

    public String newKey() {
        return  "result" + atomicInteger.incrementAndGet();
    }
}
