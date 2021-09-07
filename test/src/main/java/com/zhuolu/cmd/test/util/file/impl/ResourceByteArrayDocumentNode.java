package com.zhuolu.cmd.test.util.file.impl;

import com.zhuolu.cmd.test.util.file.DirectoryNode;

import java.io.InputStream;

public class ResourceByteArrayDocumentNode extends AbstractDocumentNode {
    public ResourceByteArrayDocumentNode(String name, DirectoryNode parent, String resource) {
        super(name, parent);
        this.resource = resource;
    }

    private final String resource;

    @Override
    public InputStream getInputStream() {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
    }
}
