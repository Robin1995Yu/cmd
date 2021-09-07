package com.zhuolu.cmd.test.util.file;

import java.io.InputStream;
import java.io.OutputStream;

public interface DocumentNode extends FileNode {
    @Override
    default boolean isDirectory() {
        return false;
    }

    @Override
    default boolean isFile() {
        return true;
    }

    InputStream getInputStream();

    OutputStream getOutputStream();
}
