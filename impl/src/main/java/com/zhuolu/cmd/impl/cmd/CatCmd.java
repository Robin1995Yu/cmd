package com.zhuolu.cmd.impl.cmd;

import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.entry.cmd.iterator.BufferedReaderIterator;
import com.zhuolu.cmd.core.CmdRuntime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CatCmd extends AbstractCmd {
    public CatCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        super("cat", previous, param, cmdRuntime);
    }

    private Set<LineHandler> lineHandlers;

    private List<String> files;

    private BufferedReader reader;

    @Override
    protected Iterator<String> doIterator() {
        return new BufferedReaderIterator(reader) {
            @Override
            protected String lineHandler(String s) {
                StringBuilder sb = new StringBuilder(s);
                for (LineHandler lineHandler : lineHandlers) {
                    sb = lineHandler.handle(sb);
                }
                return sb == null ? null : sb.toString();
            }
        };
    }

    @Override
    protected void init() {
        if (param == null || param.isEmpty()) {
            throw new IllegalArgumentException("cat's param can't be empty");
        }
        if (param.size() == 1 && param.get(0).startsWith("-")) {
            throw new IllegalArgumentException("cat need a file name");
        }
        for (int i = 1; i < param.size(); i++) {
            if (param.get(i).startsWith("-")) {
                throw new IllegalArgumentException("error option of cat");
            }
        }
        lineHandlers = new TreeSet<>();
        if (param.get(0).startsWith("-")) {
            files = param.subList(1, param.size());
            String options = param.get(0).substring(1);
            boolean bFlag = options.indexOf('b') >= 0;
            for (char c : options.toCharArray()) {
                if (c == 'n' && bFlag) {
                    continue;
                }
                lineHandlers.add(getLineHandler(c));
            }
        } else {
            files = param;
        }
        File pwd = getCmdRuntime().getPathUtil().pwd();
        List<InputStream> inputStreams = new ArrayList<>(files.size());
        for (String file : files) {
            File f = getCmdRuntime().getPathUtil().getPath(file);
            if (!f.exists()) {
                throw new IllegalArgumentException("no such file:" + f.getAbsolutePath());
            }
            if (!f.isFile()) {
                throw new IllegalArgumentException(f.getAbsolutePath() + " is not a file");
            }
            try {
                inputStreams.add(new FileInputStream(f));
            } catch (FileNotFoundException e) {
            }
        }
        reader = getBufferedReader(inputStreams);
    }

    @Override
    protected void doDestroy() {
        try {
            reader.close();
        } catch (IOException e) {
        }
    }

    private static LineHandler getLineHandler(char c) {
        //benstuv
        switch (c) {
            case 'b':
                return new BLineHandler();
            case 'e':
                return E_LINE_HANDLER;
            case 'n':
                return new NLineHandler();
            case 's':
                return new SLineHandler();
            case 't':
                return T_LINE_HANDLER;
            default:
                throw new IllegalArgumentException("do not have such option:" + c);
        }
    }

    private static abstract class LineHandler implements Comparable<LineHandler> {
        public LineHandler(Integer order) {
            this.order = order;
        }

        protected Integer order;

        public final StringBuilder handle(StringBuilder s) {
            if (s == null) {
                return null;
            }
            return doHandle(s);
        }

        protected abstract StringBuilder doHandle(StringBuilder s);

        @Override
        public final int compareTo(LineHandler o) {
            return order.compareTo(o.order);
        }
    }

    //-benstuv
    private static class BLineHandler extends LineHandler {
        private int index = 0;

        public BLineHandler() {
            super(100);
        }

        @Override
        public StringBuilder doHandle(StringBuilder s) {
            if (s.length() > 0) {
                s.insert(0, String.format("%6d", ++index) + " ");
            }
            return s;
        }
    }

    private static final LineHandler E_LINE_HANDLER = new ELineHandler();

    private static class ELineHandler extends LineHandler {
        public ELineHandler() {
            super(999);
        }

        @Override
        protected StringBuilder doHandle(StringBuilder s) {
            return s.append("$");
        }
    }

    private static class NLineHandler extends LineHandler {
        private int index = 0;

        public NLineHandler() {
            super(100);
        }

        @Override
        protected StringBuilder doHandle(StringBuilder s) {
            return s.insert(0, String.format("%6d", ++index) + " ");
        }
    }

    private static class SLineHandler extends LineHandler {
        private boolean flag = true;

        public SLineHandler() {
            super(0);
        }

        @Override
        protected StringBuilder doHandle(StringBuilder s) {
            if (s.length() > 0) {
                flag = true;
                return s;
            }
            if (s.length() <= 0 && flag) {
                flag = false;
                return s;
            }
            return null;
        }
    }

    private static final LineHandler T_LINE_HANDLER = new TLineHandler();

    private static class TLineHandler extends LineHandler {

        public TLineHandler() {
            super(1000);
        }

        @Override
        protected StringBuilder doHandle(StringBuilder s) {
            int index = 0;
            while (index >= 0 && index < s.length()) {
                if (s.charAt(index) == '\t') {
                    s.replace(index, index + 1, "^I");
                    index++;
                }
                index++;
            }
            return s;
        }
    }

    private static BufferedReader getBufferedReader(List<InputStream> inputStreams) {
        if (inputStreams == null || inputStreams.isEmpty()) {
            return null;
        }
        InputStream result = inputStreams.get(0);
        for (int i = 1; i < inputStreams.size(); i++) {
            InputStream is = inputStreams.get(i);
            result = new SequenceInputStream(result, is);
        }
        return new BufferedReader(new InputStreamReader(result));
    }
}
