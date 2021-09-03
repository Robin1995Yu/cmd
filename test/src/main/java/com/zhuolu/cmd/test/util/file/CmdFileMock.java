package com.zhuolu.cmd.test.util.file;

import com.zhuolu.cmd.core.utils.CmdFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

public abstract class CmdFileMock implements CmdFile {
    private static boolean FILE_FLAG = true;
    private static boolean DIRECTORY_FLAG = false;

    private boolean type;

    private String name;

    private CmdFileMock parent;
    private Set<CmdFileMock> children;

    @Override
    public String getAbsolutePath() {
        return parent.getAbsolutePath() + File.separator + name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<CmdFile> ls() {
        return null;
    }

    @Override
    public CmdFile getParent() {
        return null;
    }

    @Override
    public CmdFile getPath(String path) {
        return null;
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        return null;
    }

    @Override
    public OutputStream getOutputStream() throws FileNotFoundException {
        return null;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public boolean createDirectory() {
        return false;
    }

    @Override
    public boolean createDirectories() {
        return false;
    }

    @Override
    public boolean createFile() throws IOException {
        return false;
    }
}