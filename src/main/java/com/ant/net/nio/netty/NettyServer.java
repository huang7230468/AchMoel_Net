package com.ant.net.nio.netty;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.io.IOException;

/**
 * netty
 *
 * @author
 * @create 2017-12-10 22:20
 **/
public class NettyServer {

    public static void main(String[] args) throws IOException {
        NettyServer nettyServer = new NettyServer();
        nettyServer.initServer();
    }

    private void initServer() throws IOException {
        EventLoopGroup group = new NioEventLoopGroup();
    }

}
