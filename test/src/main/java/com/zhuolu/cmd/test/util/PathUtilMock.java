package com.zhuolu.cmd.test.util;

import com.zhuolu.cmd.core.utils.CmdFile;
import com.zhuolu.cmd.core.utils.PathUtil;

public class PathUtilMock implements PathUtil {
    @Override
    public CmdFile pwd() {
        return null;
    }

    @Override
    public CmdFile getPath(String path) {
        return null;
    }

    @Override
    public void cd(String path) {

    }
}
