package com.zhuolu.cmd.core.entry.cmd.iterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

public class BufferedReaderIterator implements Iterator<String> {
    private final BufferedReader reader;
    private String curr;
    private String next;

    public BufferedReaderIterator(BufferedReader reader) {
        this.reader = reader;
        next = getNext();
    }

    private String getNext() {
        try {
            String next = reader.readLine();
            while (next != null) {
                next = lineHandler(next);
                if (next == null) {
                    next = reader.readLine();
                } else {
                    break;
                }
            }
            return next;
        } catch (IOException e) {
            throw new RuntimeException("com.zhuolu.cmd.core.entry.cmd.iterator.BufferedReaderIterator readLine fail");
        }
    }

    protected String lineHandler(String s) {
        return s;
    }

    @Override
    public final boolean hasNext() {
        return next != null;
    }

    @Override
    public final String next() {
        if (hasNext()) {
            curr = next;
            next = getNext();
            return curr;
        }
        throw new IllegalStateException("iterator do not has next");
    }
}
