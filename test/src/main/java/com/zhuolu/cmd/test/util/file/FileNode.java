package com.zhuolu.cmd.test.util.file;

import com.zhuolu.cmd.core.utils.CmdFile;

public interface FileNode {
    default String getAbsolutePath() {
        return getParent() == null ? getName() : getParent().getAbsolutePath() + CmdFile.SEPARATOR + getName();
    }

    DirectoryNode getParent();

    String getName();

    boolean isFile();

    boolean isDirectory();
}
