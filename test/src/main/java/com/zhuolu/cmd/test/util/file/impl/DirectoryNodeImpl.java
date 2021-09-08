package com.zhuolu.cmd.test.util.file.impl;

import com.zhuolu.cmd.test.util.file.DirectoryNode;
import com.zhuolu.cmd.test.util.file.FileNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DirectoryNodeImpl extends AbstractFileNode implements DirectoryNode {
    public DirectoryNodeImpl(String name, DirectoryNode parent) {
        super(name, parent);
    }

    final Map<String, FileNode> children = new HashMap<>();

    @Override
    public Collection<FileNode> ls() {
        return children.values();
    }

    @Override
    public FileNode getChild(String name) {
        return children.get(name);
    }
}
