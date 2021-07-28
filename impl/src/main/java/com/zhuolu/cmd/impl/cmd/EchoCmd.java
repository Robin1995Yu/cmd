package com.zhuolu.cmd.impl.cmd;

import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.CmdRuntime;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class EchoCmd extends AbstractCmd {
    public EchoCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        super("echo", previous, param, cmdRuntime);
    }

    private List<String> lines;

    private static String join(List<String> param) {
        if (param == null || param.isEmpty()) {
            return "";
        }
        Iterator<String> iterator = param.iterator();
        StringBuilder sb = new StringBuilder(iterator.next());
        while (iterator.hasNext()) {
            sb.append(' ');
            sb.append(iterator.next());
        }
        return sb.toString();
    }

    private static String replace(String paramString) {
        return paramString;
    }

    private static List<String> split(String paramString) {
        if (paramString == null || paramString.length() == 0) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.stream(paramString.split("\n")).collect(Collectors.toList());
    }

    @Override
    public Iterator<String> doIterator() {
        return lines.iterator();
    }

    @Override
    protected void init() {
        lines = split(replace(join(param)));
    }
}
