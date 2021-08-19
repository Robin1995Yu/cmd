package com.zhuolu.cmd.net.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReplaceableInputStream extends InputStream {
    private InputStream inputStream;

    public void replace(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("param can't be null");
        }
        this.inputStream = inputStream;
    }

    @Override
    public int read(byte[] b) throws IOException {
        assertNull();
        return inputStream.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        assertNull();
        return inputStream.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        assertNull();
        return inputStream.skip(n);
    }

    @Override
    public int available() throws IOException {
        assertNull();
        return inputStream.available();
    }

    @Override
    public void close() throws IOException {
        assertNull();
        inputStream.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        assertNull();
        inputStream.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        assertNull();
        inputStream.reset();
    }

    @Override
    public boolean markSupported() {
        assertNull();
        return inputStream.markSupported();
    }

    @Override
    public int read() throws IOException {
        assertNull();
        return inputStream.read();
    }

    private void assertNull() {
        if (inputStream == null) {
            throw new IllegalStateException("inputStream can't be null");
        }
    }
}
