package com.zhuolu.cmd.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface CmdFile extends Comparable<CmdFile> {
    String SEPARATOR = File.separator;

    String getAbsolutePath();

    String getName();

    List<CmdFile> ls();

    CmdFile getParent();

    CmdFile getPath(String path);

    InputStream getInputStream() throws FileNotFoundException;

    OutputStream getOutputStream() throws FileNotFoundException;

    boolean exists();

    boolean isDirectory();

    boolean isFile();

    boolean createDirectory();

    boolean createDirectories();

    boolean createFile() throws IOException;

    @Override
    default int compareTo(CmdFile o) {
        return getAbsolutePath().compareTo(o.getAbsolutePath());
    }
}
