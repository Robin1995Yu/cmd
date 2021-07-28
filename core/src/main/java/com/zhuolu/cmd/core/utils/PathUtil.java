package com.zhuolu.cmd.core.utils;

import java.io.File;

public final class PathUtil {
    private static final File HOME = new File(System.getProperty("user.home"));
    private File pwd = HOME;

    public PathUtil() {}

    public File pwd() {
        return pwd;
    }

    public File getPath(String path) {
        File result;
        if (path.isEmpty()) {
            result = HOME;
        }else if (path.startsWith("/")) {
            result = new File(path);
        } else if (".".equals(path)) {
            result = pwd;
        } else if ("..".equals(path)) {
            result = pwd.getParentFile();
            if (result == null) {
                result = pwd;
            }
        }
        else {
            result = new File(pwd, path);
        }
        if (!result.exists()) {
            throw new IllegalArgumentException("no such file:" + result.getAbsolutePath());
        }
        return result;
    }

    public void cd(String path) {
        pwd = getPath(path);
    }
}
