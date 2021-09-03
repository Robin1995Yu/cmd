package com.zhuolu.cmd.net.server;

import com.zhuolu.cmd.core.entry.process.CmdStartProcess;
import com.zhuolu.cmd.net.handler.CmdChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Collections;
import java.util.List;

public class CmdServer {
    private final int port;
    private final int lineSize;

    private EventLoopGroup parentGroup;
    private EventLoopGroup childGroup;
    private EventLoopGroup singleGroup;

    private List<CmdStartProcess> processes;

    public void run() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        CmdChannelInitializer initializer = new CmdChannelInitializer(lineSize, processes);
        setEventLoopGroup(bootstrap)
                .channel(NioServerSocketChannel.class)
                .childHandler(initializer)
                .localAddress(port);
        ChannelFuture sync = bootstrap.bind().sync();
    }

    public static CmdServerBuilder getBuilder() {
        return new CmdServerBuilder();
    }

    private CmdServer(int parentGroupSize, int childGroupSize, int singleGroupSize, int port, int lineSize, List<CmdStartProcess> processes) {
        this.port = port;
        this.lineSize = lineSize;
        this.processes = processes == null ? Collections.emptyList() : processes;
        if (parentGroupSize < 0 && childGroupSize < 0 && singleGroupSize < 0) {
            singleGroup = new NioEventLoopGroup();
        } else if (parentGroupSize < 0 || childGroupSize < 0) {
            singleGroup = new NioEventLoopGroup(singleGroupSize);
        } else {
            parentGroup = new NioEventLoopGroup(parentGroupSize);
            childGroup = new NioEventLoopGroup(childGroupSize);
        }
    }

    private ServerBootstrap setEventLoopGroup(ServerBootstrap bootstrap) {
        if (singleGroup != null) {
            return bootstrap.group(singleGroup);
        }
        return bootstrap.group(parentGroup, childGroup);
    }

    public static class CmdServerBuilder {
        private int parentGroupSize = -1;
        private int childGroupSize = -1;
        private int singleGroupSize = -1;
        private int port = 5200;
        private int lineSize = 2048;

        private List<CmdStartProcess> processes;

        public CmdServerBuilder parentChildGroupSize(int parentGroupSize, int childGroupSize) {
            if (parentGroupSize <= 0 || childGroupSize <= 0) {
                throw new IllegalStateException("EventLoopGroup's size must be positive:" + parentGroupSize + "," + childGroupSize);
            }
            this.parentGroupSize = parentGroupSize;
            this.childGroupSize = childGroupSize;
            return this;
        }

        public CmdServerBuilder singleGroupSize(int singleGroupSize) {
            if (singleGroupSize < 0) {
                throw new IllegalStateException("EventLoopGroup's size must be positive:" + singleGroupSize);
            }
            this.singleGroupSize = singleGroupSize;
            return this;
        }

        public CmdServerBuilder port(int port) {
            this.port = port;
            return this;
        }

        public CmdServerBuilder lineSize(int lineSize) {
            this.lineSize = lineSize;
            return this;
        }

        public CmdServerBuilder processes(List<CmdStartProcess> processes) {
            this.processes = processes;
            return this;
        }

        public CmdServer build() {
            return new CmdServer(parentGroupSize, childGroupSize, singleGroupSize, port, lineSize, processes);
        }

    }
}
