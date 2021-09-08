package com.zhuolu.cmd.test.util.file;

import java.util.Collection;

public interface DirectoryNode extends FileNode {
    @Override
    default boolean isDirectory() {
        return true;
    }

    @Override
    default boolean isFile() {
        return false;
    }

    Collection<FileNode> ls();

    FileNode getChild(String name);
}
