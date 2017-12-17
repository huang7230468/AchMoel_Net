package com.ant.net.nio;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 解决粘包或分包问题
 *
 * @author
 * @create 2017-12-12 21:58
 **/
public class ServerMessageHandel extends SimpleChannelInboundHandler {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }
}
