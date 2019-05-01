package com.paulturner.nanorest;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

import com.paulturner.nanorest.http.RequestParser;

public class ServerSession {
    public AsynchronousServerSocketChannel channelServer;
    public AsynchronousSocketChannel channelClient;
    public boolean isReadMode;
    public ByteBuffer buffer;
    public SocketAddress clientSocketAddress;
    public RequestParser requestParser;

    public ServerSession(
        AsynchronousServerSocketChannel channelServer,
        AsynchronousSocketChannel channelClient,
        boolean isReadMode, ByteBuffer buffer,
        SocketAddress clientSocketAddress,
        RequestParser requestParser
    ) {
        this.channelServer = channelServer;
        this.channelClient = channelClient;
        this.isReadMode = isReadMode;
        this.buffer = buffer;
        this.clientSocketAddress = clientSocketAddress;
        this.requestParser = requestParser;
    }

    public ServerSession(
        AsynchronousServerSocketChannel channelServer
    ) {
        this.channelServer = channelServer;
    }

}