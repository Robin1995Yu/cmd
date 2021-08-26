package com.zhuolu.cmd.net.handler;

import com.zhuolu.cmd.core.CmdRuntime;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

@Sharable
public class CmdChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final LineBasedFrameDecoder lineBasedFrameDecoder;

    public CmdChannelInitializer(int lineSize) {
        lineBasedFrameDecoder = new LineBasedFrameDecoder(lineSize);
    }

    @Override
    protected void initChannel(SocketChannel serverChannel) throws Exception {
        CmdRuntime runtime = CmdRuntime.create(null, null);
        ChannelPipeline pipeline = serverChannel.pipeline();
        pipeline
                .addLast(lineBasedFrameDecoder)
                .addLast(new LineHeadChannalHandler(runtime))
                .addLast(new ToStringCodc())
                .addLast(new CmdChannelInboundHandler(runtime));
    }
}
