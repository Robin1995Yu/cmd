package com.zhuolu.cmd.net.handler;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.process.CmdStartProcess;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.util.List;

@Sharable
public class CmdChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final int lineSize;
    private final List<CmdStartProcess> processes;
    public CmdChannelInitializer(int lineSize, List<CmdStartProcess> processes) {
        this.lineSize = lineSize;
        this.processes = processes;
    }

    @Override
    protected void initChannel(SocketChannel serverChannel) throws Exception {
        CmdRuntime runtime = CmdRuntime.create(processes);
        ChannelPipeline pipeline = serverChannel.pipeline();
        pipeline
                .addLast(new LineBasedFrameDecoder(lineSize))
                .addLast(new LineHeadChannalHandler(runtime))
                .addLast(new ToStringCodc())
                .addLast(new CmdChannelInboundHandler(runtime));
    }
}
