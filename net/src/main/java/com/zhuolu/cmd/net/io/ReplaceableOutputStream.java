package com.zhuolu.cmd.net.io;

import java.io.IOException;
import java.io.OutputStream;

public class ReplaceableOutputStream extends OutputStream {
    private OutputStream outputStream;

    @Override
    public void write(int b) throws IOException {
        assertNull();
        outputStream.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        assertNull();
        outputStream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        assertNull();
        outputStream.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        assertNull();
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        assertNull();
        outputStream.close();
    }

    private void assertNull() {
        if (outputStream == null) {
            throw new IllegalStateException("outputStream is null");
        }
    }

    public void replace(OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("param can't be null");
        }
        this.outputStream = outputStream;
    }
}
