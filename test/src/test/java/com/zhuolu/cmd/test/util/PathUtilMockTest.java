package com.zhuolu.cmd.test.util;

import com.zhuolu.cmd.core.utils.PathUtil;
import com.zhuolu.cmd.test.util.file.impl.CmdFileMock;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PathUtilMockTest {
    PathUtil pathUtil = new PathUtilMock();

    @Test
    public void pwd() {
        Assert.assertEquals(pathUtil.pwd(), new CmdFileMock("/Users/robin"));
    }

    @Test
    public void getPath() {
        new CmdFileMock("/Users/robin/getPath").createDirectory();
        Assert.assertEquals(pathUtil.getPath("getPath"), new CmdFileMock("/Users/robin/getPath"));
        Assert.assertEquals(pathUtil.getPath("/"), new CmdFileMock("/"));
        Assert.assertEquals(pathUtil.getPath("."), pathUtil.pwd());
        Assert.assertEquals(pathUtil.getPath(".."), new CmdFileMock("/Users"));
    }

    @Test
    public void cd() {
        pathUtil.cd(".");
        Assert.assertEquals(pathUtil.pwd(), new CmdFileMock("/Users/robin"));
        pathUtil.cd("..");
        Assert.assertEquals(pathUtil.pwd(), new CmdFileMock("/Users"));
        pathUtil.cd("/");
        Assert.assertEquals(pathUtil.pwd(), new CmdFileMock("//"));
    }
}