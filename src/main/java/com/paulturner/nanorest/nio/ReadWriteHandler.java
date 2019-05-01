package com.paulturner.nanorest.nio;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paulturner.nanorest.ServerSession;
import com.paulturner.nanorest.http.RequestParser;
import com.paulturner.nanorest.rest.RequestRouter;

public class ReadWriteHandler
    implements CompletionHandler<Integer, ServerSession> {

    private static final Logger logger = Logger.getLogger(ReadWriteHandler.class.getName());
    private final RequestRouter requestRouter;


    public ReadWriteHandler(final RequestRouter requestRouter) {
        this.requestRouter = requestRouter;
    }

    private void requestToResponse(final RequestParser requestParser, final ServerSession serverSession) {
        serverSession.buffer.clear();
        requestRouter.handleRequest(requestParser.httpRequest()).writeToBuffer(serverSession.buffer);
        serverSession.buffer.flip();
    }

    @Override
    public void completed(final Integer result, final ServerSession serverSession) {
        if (result == -1) {
            try {
                ByteBufferPool.getInstance().release(serverSession.buffer);
                serverSession.channelClient.close();
                logger.log(Level.WARNING, "Client received EOS [client={0}]",
                    serverSession.clientSocketAddress
                );
            } catch (final IOException ioe) {
                logger.log(Level.WARNING, "Client received EOS, trouble closing Async Socket Channel", ioe);
            }
            return;
        }

        if (serverSession.isReadMode) {
            serverSession.buffer.flip();
            boolean requestParseComplete = serverSession.requestParser.parse();
            if (requestParseComplete) {
                serverSession.isReadMode = false;
                requestToResponse(serverSession.requestParser, serverSession);

                serverSession.channelClient.write(serverSession.buffer, serverSession, this);

                serverSession.buffer.clear();
                serverSession.requestParser = new RequestParser(serverSession.buffer);

            } else {
                serverSession.channelClient.read(serverSession.buffer, serverSession, this);
            }

        } else {
            serverSession.isReadMode = true;

            serverSession.buffer.clear();
            serverSession.channelClient.read(serverSession.buffer, serverSession, this);
        }
    }

    @Override
    public void failed(final Throwable t, final ServerSession att) {

        t.printStackTrace();
        System.out.println(att);
    }

}