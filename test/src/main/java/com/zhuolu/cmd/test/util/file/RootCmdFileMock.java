package com.zhuolu.cmd.test.util.file;

public class RootCmdFileMock extends DirectoryCmdFileMock {
    private static RootCmdFileMock ROOT_CMD_FILE_MOCK;

    private RootCmdFileMock() {
        super("/", null);
    }

    public static RootCmdFileMock getInstance() {
        if (ROOT_CMD_FILE_MOCK == null) {
            synchronized (RootCmdFileMock.class) {
                if (ROOT_CMD_FILE_MOCK == null) {
                    ROOT_CMD_FILE_MOCK = new RootCmdFileMock();
                }
            }
        }
        return ROOT_CMD_FILE_MOCK;
    }

    @Override
    public String getAbsolutePath() {
        return getName();
    }
}
