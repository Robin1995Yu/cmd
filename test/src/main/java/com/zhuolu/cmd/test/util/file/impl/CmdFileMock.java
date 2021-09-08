package com.zhuolu.cmd.test.util.file.impl;

import com.zhuolu.cmd.core.utils.CmdFile;
import com.zhuolu.cmd.test.util.file.DirectoryNode;
import com.zhuolu.cmd.test.util.file.DocumentNode;
import com.zhuolu.cmd.test.util.file.FileNode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CmdFileMock implements CmdFile {
    private static final DirectoryNode ROOT = new DirectoryNodeImpl("", null);

    private final String path;
    FileNode fileNode;

    public CmdFileMock(String path) {
        if (CmdFile.SEPARATOR.equals(path)) {
            this.path = path;
        } else {
            path = path.startsWith(CmdFile.SEPARATOR) ? path : CmdFile.SEPARATOR + path;
            this.path = path.endsWith(CmdFile.SEPARATOR) ? path.substring(0, path.length() - 1) : path;
        }
        fileNode = getFileNode(this.path);
    }

    private static FileNode getFileNode(String path) {
        DirectoryNode dn = ROOT;
        if (CmdFile.SEPARATOR.equals(path) || "".equals(path)) {
            return dn;
        }
        String[] split = path.substring(1).split(CmdFile.SEPARATOR);
        for (int i = 0; i < split.length; i++) {
            FileNode child = dn.getChild(split[i]);
            if (child == null) {
                return null;
            }
            if (child instanceof DirectoryNode) {
                dn = (DirectoryNode) child;
            } else if (i == split.length - 1) {
                return child;
            } else {
                throw new IllegalArgumentException(split[i] + " is not a dir");
            }
        }
        return dn;
    }

    private CmdFileMock(String path, FileNode fileNode) {
        this.path = path.endsWith(CmdFile.SEPARATOR) ? path.substring(0, path.length() - 1) : path;
        this.fileNode = fileNode;
    }

    @Override
    public String getAbsolutePath() {
        return path;
    }

    @Override
    public String getName() {
        String s = path;
        if (s.endsWith(CmdFile.SEPARATOR)) {
            s = s.substring(0, s.length() - 1);
        }
        return s.substring(s.lastIndexOf(CmdFile.SEPARATOR) + 1);
    }

    @Override
    public Collection<CmdFile> ls() {
        if (fileNode == null || fileNode.isFile()) {
            throw new UnsupportedOperationException();
        }
        return ((DirectoryNode) fileNode).ls().stream().map(
                fileNode -> new CmdFileMock(fileNode.getAbsolutePath(), fileNode)
        ).collect(Collectors.toSet());
    }

    @Override
    public CmdFile getParent() {
        if (ROOT.equals(fileNode)) {
            return null;
        }
        return new CmdFileMock(path.substring(0, path.lastIndexOf(CmdFile.SEPARATOR)));
    }

    @Override
    public CmdFile getPath(String path) {
        if (CmdFile.SEPARATOR.equals(this.path)) {
            return new CmdFileMock(this.path + path);
        }
        path = path.startsWith(CmdFile.SEPARATOR) ? path : CmdFile.SEPARATOR + path;
        return new CmdFileMock(this.path + path);
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        if (fileNode == null || fileNode.isDirectory()) {
            throw new UnsupportedOperationException();
        }
        return ((DocumentNode) fileNode).getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws FileNotFoundException {
        if (fileNode == null || fileNode.isDirectory()) {
            throw new UnsupportedOperationException();
        }
        return ((DocumentNode) fileNode).getOutputStream();
    }

    @Override
    public boolean exists() {
        fileNode = getFileNode(path);
        return fileNode != null;
    }

    @Override
    public boolean isDirectory() {
        return fileNode == null ? false : fileNode.isDirectory();
    }

    @Override
    public boolean isFile() {
        return fileNode == null ? false : fileNode.isFile();
    }

    @Override
    public boolean createDirectory() {
        if (exists()) {
            return false;
        }
        CmdFileMock parent = (CmdFileMock) getParent();
        if (parent.isDirectory()) {
            String name = getName();
            DirectoryNodeImpl parentFileNode = (DirectoryNodeImpl) parent.fileNode;
            DirectoryNodeImpl directoryNode = new DirectoryNodeImpl(name, parentFileNode);
            parentFileNode.children.put(name, directoryNode);
            fileNode = directoryNode;
            return true;
        }
        return false;
    }

    @Override
    public boolean createDirectories() {
        if (exists()) {
            return false;
        }
        List<CmdFile> nonExistsDirList = new LinkedList<>();
        CmdFile parent = getParent();
        while (!parent.exists()) {
            nonExistsDirList.add(0, parent);
            parent = parent.getParent();
        }
        if (parent.isFile()) {
            return false;
        }
        for (CmdFile cmdFile : nonExistsDirList) {
            if (!cmdFile.createDirectory()) {
                return false;
            }
        }
        createDirectory();
        return true;
    }

    @Override
    public boolean createFile() throws IOException {
        if (exists()) {
            return false;
        }
        CmdFileMock parent = (CmdFileMock) getParent();
        if (parent.isDirectory()) {
            String name = getName();
            DirectoryNodeImpl directoryNode = (DirectoryNodeImpl) parent.fileNode;
            ByteArrayDocumentNode byteArrayDocumentNode = new ByteArrayDocumentNode(name, directoryNode);
            fileNode = byteArrayDocumentNode;
            directoryNode.children.put(name, byteArrayDocumentNode);
            return true;
        }
        return false;
    }

    public boolean createFile(int bufSize) {
        if (exists()) {
            return false;
        }
        CmdFileMock parent = (CmdFileMock) getParent();
        if (parent.isDirectory()) {
            String name = getName();
            DirectoryNodeImpl directoryNode = (DirectoryNodeImpl) parent.fileNode;
            ByteArrayDocumentNode byteArrayDocumentNode = new ByteArrayDocumentNode(name, directoryNode, bufSize);
            fileNode = byteArrayDocumentNode;
            directoryNode.children.put(name, byteArrayDocumentNode);
            return true;
        }
        return false;
    }

    public boolean createFile(String source) {
        if (exists()) {
            return false;
        }
        CmdFileMock parent = (CmdFileMock) getParent();
        if (parent.isDirectory()) {
            String name = getName();
            DirectoryNodeImpl directoryNode = (DirectoryNodeImpl) parent.fileNode;
            ResourceDocumentNode resourceDocumentNode = new ResourceDocumentNode(name, directoryNode, source);
            fileNode = resourceDocumentNode;
            directoryNode.children.put(name, resourceDocumentNode);
            return true;
        }
        return false;
    }

    public static void clearAll() {
        ((DirectoryNodeImpl) ROOT).children.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CmdFileMock that = (CmdFileMock) o;
        return path.equals(that.path) && Objects.equals(fileNode, that.fileNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, fileNode);
    }

    @Override
    public String toString() {
        return path;
    }
}
