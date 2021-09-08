package com.zhuolu.cmd.test.util.file.impl;

import com.zhuolu.cmd.test.util.file.DirectoryNode;
import com.zhuolu.cmd.test.util.file.DocumentNode;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractDocumentNode extends AbstractFileNode implements DocumentNode {
    protected AbstractDocumentNode(String name, DirectoryNode parent) {
        super(name, parent);
    }

    @Override
    public InputStream getInputStream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public OutputStream getOutputStream() {
        throw new UnsupportedOperationException();
    }
}
