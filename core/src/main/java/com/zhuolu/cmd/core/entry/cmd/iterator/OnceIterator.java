package com.zhuolu.cmd.core.entry.cmd.iterator;

import java.util.Iterator;

public class OnceIterator implements Iterator<String> {
    private boolean flag = true;

    private String value;

    public OnceIterator(String value) {
        this.value = value;
    }

    @Override
    public boolean hasNext() {
        return flag;
    }

    @Override
    public String next() {
        flag = false;
        return value;
    }
}
