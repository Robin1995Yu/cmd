package com.zhuolu.cmd.core.utils.impl;

import com.zhuolu.cmd.core.utils.CmdFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CmdFileImpl implements CmdFile {
    private final File file;

    public CmdFileImpl(File file) {
        this.file = file;
    }

    @Override
    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public List<CmdFile> ls() {
        File[] files = file.listFiles();
        return Arrays.stream(files).map(CmdFileImpl::new).collect(Collectors.toList());
    }

    @Override
    public CmdFile getParent() {
        return new CmdFileImpl(file.getParentFile());
    }

    @Override
    public CmdFile getPath(String path) {
        return new CmdFileImpl(new File(file, path));
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(file);
    }

    @Override
    public OutputStream getOutputStream() throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    @Override
    public boolean exists() {
        return file.exists();
    }

    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }

    @Override
    public boolean isFile() {
        return file.isFile();
    }

    @Override
    public boolean createDirectory() {
        return file.mkdir();
    }

    @Override
    public boolean createDirectories() {
        return file.mkdirs();
    }

    @Override
    public boolean createFile() throws IOException {
        return file.createNewFile();
    }
}
