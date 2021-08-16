package com.zhuolu.cmd.net.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ServerChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

public class CmdChannelInitializer extends ChannelInitializer<ServerChannel> {
    private final LineBasedFrameDecoder lineBasedFrameDecoder;

    private static CmdChannelInitializer instance;

    private CmdChannelInitializer(int lineSize) {
        lineBasedFrameDecoder = new LineBasedFrameDecoder(2048);
    }

    public static void init() {
        init(2048);
    }

    public static void init(int lineSize) {
        if (instance == null) {
            synchronized (CmdChannelInitializer.class) {
                if (instance == null) {
                    instance = new CmdChannelInitializer(lineSize);
                }
            }
        }
    }

    public static CmdChannelInitializer getInstance() {
        if (instance == null) {
            init();
        }
        return instance;
    }

    @Override
    protected void initChannel(ServerChannel serverChannel) throws Exception {
        ChannelPipeline pipeline = serverChannel.pipeline();
        pipeline.addLast(lineBasedFrameDecoder);
    }
}
