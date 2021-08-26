package com.zhuolu.cmd.net.handler;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CmdChannelInboundHandler extends SimpleChannelInboundHandler<String> {
    private final CmdRuntime runtime;

    public CmdChannelInboundHandler(CmdRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        try {
            Cmd cmd = runtime.getCmdUtil().initCmd(msg);
            for (String s : cmd) {
                ctx.pipeline().writeAndFlush(s + "\n");
            }
            ctx.pipeline().flush();
        } finally {
            if (!runtime.isRunFlag()) {
                ctx.close();
                return;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.pipeline().writeAndFlush(cause.getMessage() + "\n");
    }
}
