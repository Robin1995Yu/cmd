package com.zhuolu.cmd.test.util.file.impl;

import com.zhuolu.cmd.test.util.file.DirectoryNode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteArrayDocumentNode extends AbstractDocumentNode {
    private static final int DEFAULT_BUF_SIZE = 1024;

    public ByteArrayDocumentNode(String name, DirectoryNode parent) {
        this(name, parent, DEFAULT_BUF_SIZE);
    }

    public ByteArrayDocumentNode(String name, DirectoryNode parent, int bufSize) {
        this(name, parent, new byte[bufSize]);
    }

    public ByteArrayDocumentNode(String name, DirectoryNode parent, byte[] buf) {
        super(name, parent);
        this.buf = buf;
    }

    private final byte[] buf;

    public byte[] getBuf() {
        return buf;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(buf);
    }

    @Override
    public OutputStream getOutputStream() {
        return new ByteArrayDocumentOutputStream();
    }

    private class ByteArrayDocumentOutputStream extends OutputStream {
        private int count = 0;

        @Override
        public void write(int b) throws IOException {
            if (count >= buf.length) {
                throw new IOException("oversize:" + buf.length);
            }
            buf[count++] = (byte) b;
        }
    }
}
