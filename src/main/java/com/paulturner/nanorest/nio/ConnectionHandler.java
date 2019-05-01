package com.paulturner.nanorest.nio;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paulturner.nanorest.ServerSession;
import com.paulturner.nanorest.http.RequestParser;
import com.paulturner.nanorest.rest.RequestRouter;

public class ConnectionHandler
    implements CompletionHandler<AsynchronousSocketChannel, ServerSession> {

    private static final Logger logger = Logger.getLogger(ConnectionHandler.class.getName());

    @Override
    public void completed(
        AsynchronousSocketChannel channelClient,
        ServerSession serverSession
    ) {
        try {
            SocketAddress remoteAddress = channelClient.getRemoteAddress();
            logger.log(Level.INFO, "Accepted connection [remoteAddress={0}]", remoteAddress);

            serverSession.channelServer.accept(serverSession, this);

            ByteBuffer buffer = ByteBufferPool.getInstance().acquire();
            ServerSession newConnectionServerSession = new ServerSession(
                serverSession.channelServer, channelClient, true, buffer,
                remoteAddress, new RequestParser(buffer)
            );

            ReadWriteHandler readWriteHandler = new ReadWriteHandler(RequestRouter.getInstance());
            channelClient.read(newConnectionServerSession.buffer, newConnectionServerSession, readWriteHandler);

        } catch (IOException ioe) {
            logger.log(Level.WARNING, "ConnectionHandler callback failed", ioe);
        }
    }

    @Override
    public void failed(Throwable t, ServerSession serverSession) {

        logger.log(Level.WARNING, "Connect to client failed", t);
        logger.log(Level.WARNING, "State on failure", serverSession);
    }
}