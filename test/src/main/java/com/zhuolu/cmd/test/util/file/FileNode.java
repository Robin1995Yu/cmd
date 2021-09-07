package com.zhuolu.cmd.test.util.file;

public interface FileNode {
    DirectoryNode getParent();

    String getName();

    boolean isFile();

    boolean isDirectory();
}
