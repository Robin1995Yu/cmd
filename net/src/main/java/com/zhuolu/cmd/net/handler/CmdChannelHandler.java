package com.zhuolu.cmd.net.handler;

import com.zhuolu.cmd.core.CmdRuntime;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.TreeMap;

@Sharable
public class CmdChannelHandler extends ChannelInboundHandlerAdapter {
    private final Map<Channel, CmdRuntimeHandler> map = new TreeMap<>();
    private final int lineSize;

    public CmdChannelHandler(int lineSize) {
        this.lineSize =lineSize;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] readBuf = new byte[lineSize];
        byte[] writeBuf = new byte[lineSize];
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(readBuf)));
        OutputStream os = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
        CmdRuntime.create(reader, writer);

    }


}
