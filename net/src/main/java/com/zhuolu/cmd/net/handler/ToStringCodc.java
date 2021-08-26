package com.zhuolu.cmd.net.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.CharsetUtil;

import java.util.List;

public class ToStringCodc extends ByteToMessageCodec<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        out.writeCharSequence(msg, CharsetUtil.UTF_8);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String line = in.readCharSequence(in.readableBytes(), CharsetUtil.UTF_8).toString();
        out.add(line);
    }
}
