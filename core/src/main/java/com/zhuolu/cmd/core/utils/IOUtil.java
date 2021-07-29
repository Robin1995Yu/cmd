package com.zhuolu.cmd.core.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public final class IOUtil implements AutoCloseable {
    private BufferedReader reader;

    private BufferedWriter writer;

    public IOUtil(BufferedReader reader, BufferedWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public String reader() {
        try {
            return reader.readLine();
        } catch (IOException e) {
        }
        return null;
    }

    public void write(String line) {
        try {
            writer.write(line);
        } catch (IOException e) {
        }
    }

    public void newLine() {
        try {
            writer.newLine();
        } catch (IOException e) {
        }
    }

    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
        }
    }

    @Override
    public void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
        if (writer != null) {
            writer.close();
        }
    }
}
