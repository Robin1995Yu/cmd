package com.zhuolu.cmd.core.utils.impl;

import com.zhuolu.cmd.core.utils.PathUtil;

import java.io.File;

public final class PathUtilImpl implements PathUtil {
    private static final File HOME = new File(System.getProperty("user.home"));
    private File pwd = HOME;

    public PathUtilImpl() {}

    @Override
    public File pwd() {
        return pwd;
    }

    @Override
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

    @Override
    public void cd(String path) {
        pwd = getPath(path);
    }
}
