package com.zhuolu.cmd.core.utils;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.factory.CmdFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class CmdUtil {
    private final CmdRuntime cmdRuntime;
    private final Map<String, CmdFactory> cmdFactoryMap;

    public CmdUtil(CmdRuntime cmdRuntime) {
        this.cmdRuntime = cmdRuntime;
        ServiceLoader<CmdFactory> loader = ServiceLoader.load(CmdFactory.class);
        Map<String, CmdFactory> m = new TreeMap<>();
        for (CmdFactory factory : loader) {
            m.put(factory.getName(), factory);
        }
        cmdFactoryMap = m;
    }

    public void invoke(String cmd) {
        invoke(initCmd(cmd));
    }

    private void invoke(Cmd cmd) {
        for (String line : cmd) {
            cmdRuntime.getIoUtil().write(line);
            cmdRuntime.getIoUtil().newLine();
        }
        cmdRuntime.getIoUtil().flush();
        cmd.destroy();
    }

    private Cmd initCmd(String line) {
        List<String> strings = splitPipe(line);
        Cmd cmd = null;
        for (String s : strings) {
            int i = s.indexOf(' ');
            String name;
            String param;
            if (i < 0) {
                name = s;
                param = "";
            } else {
                name = s.substring(0, i);
                param = s.substring(i + 1);
            }
            cmd = getCmd(name, cmd, splitParam(param));
        }
        return cmd;
    }

    private List<String> splitPipe(String line) {
        List<String> result = new ArrayList<>();
        line = line.trim();
        int index = 0;
        int lastSplit = -1;
        boolean flag = true;
        while (index < line.length()) {
            char c = line.charAt(index);
            switch (c) {
                case '"':
                    flag = !flag;
                    break;
                case '\\':
                    index++;
                    break;
                case '|':
                    if (flag) {
                        result.add(line.substring(lastSplit + 1, index).trim());
                        lastSplit = index;
                    }
                    break;
            }
            index++;
        }
        result.add(line.substring(lastSplit + 1).trim());
        return result;
    }

    private List<String> splitParam(String paramString) {
        paramString = paramString.trim();
        if (paramString.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> param = new ArrayList<>();
        int s = 0;
        out: while (true) {
            char c = paramString.charAt(s);
            switch (c) {
                case '"':
                    int e = nextSplitChar(s, paramString);
                    if (e >= 0) {
                        param.add(paramString.substring(s + 1, e));
                        s = nextNotEmptyChar(e, paramString);
                        if (s < 0) {
                            break out;
                        }
                    } else {
                        throw new IllegalArgumentException("\" not close:" + s);
                    }
                    break;
                case ' ':
                case '\t':
                    s = nextNotEmptyChar(s, paramString);
                    break;
                default:
                    int end = nextEndChar(s, paramString);
                    if (end >= 0) {
                        param.add(paramString.substring(s, end));
                    } else {
                        param.add(paramString.substring(s));
                        break out;
                    }
                    char endChar = paramString.charAt(end);
                    if (endChar == '"') {
                        s = end;
                    } else {
                        s = nextNotEmptyChar(end, paramString);
                    }
            }
        }
        return param.stream().map(p -> replaceExport(p)).collect(Collectors.toList());
    }

    public Cmd getCmd(String name, Cmd previous, List<String> param) {
        CmdFactory cmdFactory = cmdFactoryMap.get(name);
        if (cmdFactory == null) {
            throw new IllegalArgumentException("no such cmd:" + name);
        }
        return cmdFactory.getCmd(previous, param, cmdRuntime);
    }

    private int nextSplitChar(int start, String s) {
        int index = start + 1;
        while (index < s.length()) {
            char c = s.charAt(index);
            if (c == '\\') {
                index++;
            } else if (c == '"') {
                return index;
            }
            index++;
        }
        return -1;
    }

    private int nextNotEmptyChar(int start, String s) {
        for (int i = start + 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != ' ' && c != '\t') {
                return i;
            }
        }
        return -1;
    }

    private int nextEndChar(int start, String s) {
        int index = start + 1;
        while (index < s.length()) {
            char c = s.charAt(index);
            if (c == '\\') {
                index++;
            } else if (c == '"' || c == ' ' || c == '\t') {
                return index;
            }
            index++;
        }
        return -1;
    }

    private String replaceExport(String param) {
        StringBuilder sb = new StringBuilder(param);
        int index = 0;
        while (index + 1 < sb.length()) {
            char c = sb.charAt(index);
            if (c == '\\') {
                index++;
            } else if (c == '$' && sb.charAt(index + 1) == '{') {
                int end = nextEnd(sb, index + 1);
                if (end < 0) {
                    throw new IllegalArgumentException("don't has end of {: " + index + 1);
                }
                String elResult = executeEl(sb.substring(index + 2, end));
                sb.replace(index, end + 1, elResult);
                index = end + 1;
            }
            index++;
        }
        return format(sb);
    }

    private int nextEnd(StringBuilder sb, int start) {
        int index = start + 1;
        while (index < sb.length()) {
            char c = sb.charAt(index);
            if (c == '\\') {
                index++;
            } else if (c == '}') {
                return index;
            }
            index++;
        }
        return -1;
    }

    private String executeEl(String el) {
        el = format(new StringBuilder(el));
        Object result = cmdRuntime.getExportContextUtil().get(el);
        if (result == null) {
            result = el;
        }
        return result.toString();
    }

    private String format(StringBuilder sb) {
        int index = 0;
        while (index < sb.length()) {
            if (sb.charAt(index) == '\\') {
                sb.deleteCharAt(index);
                index++;
            }
            index++;
        }
        return sb.toString();
    }
}
