package com.zhuolu.cmd.impl.cmd;

import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.CmdRuntime;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class LsCmd extends AbstractCmd {
    public LsCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        super("ls", previous, param, cmdRuntime);
    }

    private Map<File, Iterator<File>> fileMap;

    @Override
    protected void init() {
        fileMap = new TreeMap<>();
        File pwd = getCmdRuntime().getPathUtil().pwd();
        if (param.isEmpty()) {
            fileMap.put(pwd, null);
        } else {
            for (String fileName : param) {
                File f = getCmdRuntime().getPathUtil().getPath(fileName);
                if (!f.exists() || !f.isDirectory()) {
                    throw new IllegalArgumentException(fileName + " is not exists or is a dictory");
                }
                fileMap.put(f, null);
            }
        }
    }

    @Override
    protected Iterator<String> doIterator() {
        return new Itr();
    }

    private class Itr implements Iterator<String> {
        private Iterator<Map.Entry<File, Iterator<File>>> entryIterator = fileMap.entrySet().iterator();
        private Iterator<File> fileIterator;

        private String curr;
        private String next;


        {
            Map.Entry<File, Iterator<File>> root = entryIterator.next();
            fileIterator = doLs(root);
            next = root.getKey().getName() + ":";
            if (!entryIterator.hasNext()) {
                next();
            }
        }

        @Override
        public boolean hasNext() {
            return entryIterator.hasNext() || fileIterator.hasNext();
        }

        @Override
        public String next() {
            curr = next;
            next = doNext();
            return curr;
        }
        private String doNext() {
            if (!hasNext()) {
                return null;
            }
            if (fileIterator.hasNext()) {
                return fileIterator.next().getName();
            }
            Map.Entry<File, Iterator<File>> root = entryIterator.next();
            fileIterator = doLs(root);
            return root.getKey().getName() + ":";
        }

    }

    private static Iterator<File> doLs(Map.Entry<File, Iterator<File>> entry) {
        if (entry.getValue() == null) {
            entry.setValue(doLs(entry.getKey()));
        }
        return entry.getValue();
    }

    private static Iterator<File> doLs(File path) {
        return Arrays.stream(path.listFiles()).collect(Collectors.toList()).iterator();
    }
}
