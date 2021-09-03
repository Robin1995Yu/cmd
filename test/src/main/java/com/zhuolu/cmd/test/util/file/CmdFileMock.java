package com.zhuolu.cmd.test.util.file;

import com.zhuolu.cmd.core.utils.CmdFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public abstract class CmdFileMock implements CmdFile {
    private final String name;

    private final DirectoryCmdFileMock parent;

    protected CmdFileMock(String name, DirectoryCmdFileMock parent) {
        this.name = name;
        this.parent = parent;
    }

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
        throw new UnsupportedOperationException("ls");
    }

    @Override
    public DirectoryCmdFileMock getParent() {
        return parent;
    }

    @Override
    public CmdFile getPath(String path) {
        throw new UnsupportedOperationException("getPath");
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        throw new UnsupportedOperationException("getInputStream");
    }

    @Override
    public OutputStream getOutputStream() throws FileNotFoundException {
        throw new UnsupportedOperationException("getInputStream");
    }

    @Override
    public boolean exists() {
        return parent == null ? false : parent.ls().contains(this);
    }

    @Override
    public boolean isDirectory() {
        throw new UnsupportedOperationException("isDirectory");
    }

    @Override
    public boolean isFile() {
        throw new UnsupportedOperationException("isFile");
    }

    @Override
    public boolean createDirectory() {
        throw new UnsupportedOperationException("createDirectory");
    }

    @Override
    public boolean createDirectories() {
        throw new UnsupportedOperationException("createDirectories");
    }

    @Override
    public boolean createFile() throws IOException {
        throw new UnsupportedOperationException("createFile");
    }
}