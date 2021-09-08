package com.zhuolu.cmd.test.util.file.impl;

import com.zhuolu.cmd.test.util.file.DirectoryNode;
import com.zhuolu.cmd.test.util.file.FileNode;

public abstract class AbstractFileNode implements FileNode {
    private final String name;
    private final DirectoryNode parent;

    protected AbstractFileNode(String name, DirectoryNode parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DirectoryNode getParent() {
        return parent;
    }
}
