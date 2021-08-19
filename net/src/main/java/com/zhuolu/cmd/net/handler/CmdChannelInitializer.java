package com.zhuolu.cmd.net.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ServerChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

public class CmdChannelInitializer extends ChannelInitializer<ServerChannel> {
    private final LineBasedFrameDecoder lineBasedFrameDecoder;

    private static CmdChannelInitializer instance;

    public CmdChannelInitializer(int lineSize) {
        lineBasedFrameDecoder = new LineBasedFrameDecoder(2048);
    }

    @Override
    protected void initChannel(ServerChannel serverChannel) throws Exception {
        ChannelPipeline pipeline = serverChannel.pipeline();
        pipeline.addLast(lineBasedFrameDecoder);
    }
}
