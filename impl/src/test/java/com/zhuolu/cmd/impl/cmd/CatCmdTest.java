package com.zhuolu.cmd.impl.cmd;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.impl.factory.CatCmdFactory;
import com.zhuolu.cmd.test.CmdRuntimeMock;
import com.zhuolu.cmd.test.util.file.impl.CmdFileMock;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class CatCmdTest {
    private CmdRuntime cmdRuntime = new CmdRuntimeMock();

    private CatCmdFactory catCmdFactory = new CatCmdFactory();

    {
        new CmdFileMock("/catFile").createFile("catTest.txt");
    }

    @Test
    public void test() {
        Cmd cmd = catCmdFactory.getCmd(null, Collections.singletonList("/catFile"), cmdRuntime);
        for (String s : cmd) {
            assertEquals(s, "this is cat Mock");
        }
        List<String> params = new ArrayList<>(2);
        params.add("-n");
        params.add("/catFile");
        Cmd cmdN = catCmdFactory.getCmd(null, params, cmdRuntime);
        int count = 0;
        for (String s : cmdN) {
            assertEquals(s, String.format("%6d ", ++count) + "this is cat Mock");
        }
    }
}