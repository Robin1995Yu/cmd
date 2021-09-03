package com.zhuolu.cmd.test.util.file;

import com.zhuolu.cmd.core.utils.CmdFile;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DirectoryCmdFileMock extends CmdFileMock {
    private List<CmdFileMock> children = new LinkedList<>();

    public DirectoryCmdFileMock(String name, DirectoryCmdFileMock parent) {
        super(name, parent);
    }

    @Override
    public List<CmdFile> ls() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public boolean isFile() {
        return false;
    }


}
