package com.zhuolu.cmd.impl.cmd;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.entry.cmd.iterator.BufferedReaderIterator;
import com.zhuolu.cmd.impl.factory.ResultCmdFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ResultCmd extends AbstractCmd {
    private static final Map<String, ResultHandler> RESULT_HANDLER_MAP;

    static {
        RESULT_HANDLER_MAP = new HashMap<>(4);
        RESULT_HANDLER_MAP.put("get", (cmdRuntime, resultKey, exportKey) -> {
            ResultCmdFactory resultCmdFactory = (ResultCmdFactory) cmdRuntime.getCmdUtil().getCmdFactory("result");
            return resultCmdFactory.getResult(resultKey);
        });
        RESULT_HANDLER_MAP.put("export", (cmdRuntime, resultKey, exportKey) -> {
            ResultCmdFactory resultCmdFactory = (ResultCmdFactory) cmdRuntime.getCmdUtil().getCmdFactory("result");
            Object r = resultCmdFactory.getResult(resultKey);
            if (r != null) {
                cmdRuntime.getExportContextUtil().set(exportKey, r);
                resultCmdFactory.delResult(resultKey);
            }
            return r;
        });
        RESULT_HANDLER_MAP.put("del", (cmdRuntime, resultKey, exportKey) -> {
            ResultCmdFactory resultCmdFactory = (ResultCmdFactory) cmdRuntime.getCmdUtil().getCmdFactory("result");
            return resultCmdFactory.delResult(resultKey);
        });
    }

    private ResultHandler resultHandler;

    private String result;

    private String resultKey;
    private String exportKey;

    public ResultCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        super("result", previous, param, cmdRuntime);
    }

    @Override
    protected void init() {
        if (param == null || param.size() < 2) {
            throw new IllegalArgumentException("result must has option and key");
        }
        resultHandler = RESULT_HANDLER_MAP.get(param.get(0));
        if (resultHandler == null) {
            throw new IllegalArgumentException("no such option:" + param.get(0));
        }
        resultKey = param.get(1);
        if (param.get(0).equals("export")) {
            if (param.size() < 3) {
                throw new IllegalArgumentException("result export must has resultKey and exportKey");
            }
            exportKey = param.get(2);
        }
    }

    @Override
    public void invoke() {
        Object v = resultHandler.handle(getCmdRuntime(), resultKey, exportKey);
        if (v != null) {
            result = param.get(0) + " success!\n" + v;
        } else {
            result = "no such key " + param.get(0);
        }
    }

    private interface ResultHandler {
        Object handle(CmdRuntime cmdRuntime, String resultKey, String exportKey);
    }

    @Override
    protected Iterator<String> doIterator() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8))));
        return new BufferedReaderIterator(reader);
    }
}
