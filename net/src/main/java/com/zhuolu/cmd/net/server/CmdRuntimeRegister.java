package com.zhuolu.cmd.net.server;

import com.zhuolu.cmd.net.handler.CmdRuntimeHandler;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CmdRuntimeRegister {
    public static final Map<Channel, CmdRuntimeHandler> MAP = new ConcurrentHashMap<>();


}
