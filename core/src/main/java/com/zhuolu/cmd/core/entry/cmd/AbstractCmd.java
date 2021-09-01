package com.zhuolu.cmd.core.entry.cmd;

import com.zhuolu.cmd.core.CmdRuntime;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractCmd implements Cmd {
    public AbstractCmd(String name, Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        this.name = name;
        this.previous = previous;
        this.param = param;
        this.cmdRuntime = cmdRuntime;
        init();
    }

    @Override
    public final Iterator<String> iterator() {
        invoke();
        return doIterator();
    }

    protected Iterator<String> doIterator() {
        return Collections.emptyIterator();
    }

    private final String name;

    /**
     * 前一个命令
     */
    private final Cmd previous;

    protected List<String> param;

    private final CmdRuntime cmdRuntime;

    protected void init() {}

    protected boolean assertNext(String nextLine) {
        return true;
    }

    protected String preNextReturn(String nextLine) {
        return nextLine;
    }

    protected final Iterator<String> getPreviousIterator() {
        if (previous == null) {
            return Collections.emptyIterator();
        }
        Iterator<String> result = previous.iterator();
        return result == null ? Collections.emptyIterator() : result;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public void invoke() {
    }

    @Override
    public final void destroy() {
        if (previous != null) {
            previous.destroy();
        }
        doDestroy();
    }

    @Override
    public final CmdRuntime getCmdRuntime() {
        return cmdRuntime;
    }

    protected void doDestroy() {}

    protected class CmdIterator implements Iterator<String> {
        private Iterator<String> previousIterator = AbstractCmd.this.getPreviousIterator();
        private String curr;
        private String next;

        public CmdIterator() {
            next = getNextString();
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public String next() {
            if (hasNext()) {
                curr = next;
                next = getNextString();
                return curr;
            }
            throw new IllegalStateException("iterator do not has next");
        }

        private String getNextString() {
            while (previousIterator.hasNext()) {
                String nextLine = previousIterator.next();
                if (nextLine == null) {
                    nextLine = "";
                }
                if (assertNext(nextLine)) {
                    return preNextReturn(nextLine);
                }
            }
            return null;
        }
    }
}
