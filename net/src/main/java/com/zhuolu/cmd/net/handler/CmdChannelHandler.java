package com.zhuolu.cmd.net.handler;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.net.io.ReplaceableInputStream;
import com.zhuolu.cmd.net.io.ReplaceableOutputStream;
import com.zhuolu.cmd.net.server.CmdRuntimeRegister;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

@Sharable
public class CmdChannelHandler extends SimpleChannelInboundHandler<NioServerSocketChannel> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, NioServerSocketChannel channel) throws Exception {
        ReplaceableInputStream inputStream = new ReplaceableInputStream();
        ReplaceableOutputStream outputStream = new ReplaceableOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        CmdRuntime cmdRuntime = CmdRuntime.create(reader, writer);
        CmdRuntimeRegister.MAP.put(channel, new CmdRuntimeHandler(cmdRuntime, inputStream, outputStream));
    }
}
