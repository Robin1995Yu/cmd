package com.zhuolu.cmd.core.utils.impl;

import com.zhuolu.cmd.core.utils.CmdFile;
import com.zhuolu.cmd.core.utils.PathUtil;

import java.io.File;

public final class PathUtilImpl implements PathUtil {
    private static final CmdFile HOME = new CmdFileImpl(new File(System.getProperty("user.home")));
    private CmdFile pwd = HOME;

    public PathUtilImpl() {}

    @Override
    public CmdFile pwd() {
        return pwd;
    }

    @Override
    public CmdFile getPath(String path) {
        CmdFile result;
        if (path.isEmpty()) {
            result = HOME;
        }else if (path.startsWith("/")) {
            result = new CmdFileImpl(new File(path));
        } else if (".".equals(path)) {
            result = pwd;
        } else if ("..".equals(path)) {
            result = pwd.getParent();
            if (result == null) {
                result = pwd;
            }
        }
        else {
            result = pwd.getPath(path);
        }
        if (!result.exists()) {
            throw new IllegalArgumentException("no such file:" + result.getAbsolutePath());
        }
        return result;
    }

    @Override
    public void cd(String path) {
        CmdFile t = getPath(path);
        if (t.isDirectory()) {
            pwd = t;
        } else {
            throw new IllegalArgumentException(t.getAbsolutePath() + " is not a directory");
        }

    }
}
