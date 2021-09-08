package com.zhuolu.cmd.test.util;

import com.zhuolu.cmd.core.utils.CmdFile;
import com.zhuolu.cmd.core.utils.PathUtil;
import com.zhuolu.cmd.core.utils.impl.CmdFileImpl;
import com.zhuolu.cmd.test.util.file.impl.CmdFileMock;

import java.io.File;
import java.io.IOException;

public class PathUtilMock implements PathUtil {
    private static final CmdFile HOME = new CmdFileMock("/");

    static {
        HOME.createDirectories();
    }

    private CmdFile pwd = HOME;

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
            result = new CmdFileMock(path);
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
        CmdFile file = getPath(path);
        if (file.isDirectory()) {
            pwd = file;
            return;
        }
        throw new IllegalArgumentException(path + " is not a directory");
    }
}
