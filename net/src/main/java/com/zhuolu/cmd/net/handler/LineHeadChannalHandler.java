package com.zhuolu.cmd.net.handler;

import com.zhuolu.cmd.core.CmdRuntime;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LineHeadChannalHandler extends ChannelInboundHandlerAdapter {
    private final CmdRuntime runtime;

    public LineHeadChannalHandler(CmdRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        String name = runtime.getPathUtil().pwd().getName();
        if (name.length() == 0) {
            name = "/";
        }
        ctx.pipeline().writeAndFlush("->  " + name + ">>");
    }
}
