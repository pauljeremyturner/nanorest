package com.paulturner.nanorest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paulturner.nanorest.configuration.ApplicationConfiguration;
import com.paulturner.nanorest.nio.ConnectionHandler;

public class Server implements Runnable {

    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final int DEFAULT_PORT = 8080;
    private final CountDownLatch stopLatch = new CountDownLatch(1);

    public void run() {

        int port;
        String configuredPortString = System.getProperty(ApplicationConfiguration.KEY_PORT);
        if (Objects.isNull(configuredPortString)) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(configuredPortString);
        }
        logger.log(Level.INFO, "Server listening [port = {0}]", port);

        AsynchronousServerSocketChannel socketChannel;
        try {
            socketChannel = AsynchronousServerSocketChannel.open();
            final InetSocketAddress address = new InetSocketAddress(port);
            socketChannel.bind(address);
            logger.log(
                Level.INFO,
                "Started Server [address={0}] [port={1}]",
                new Object[]{address, port});
        } catch (IOException ioe) {
            logger.log(
                Level.INFO,
                "Could not bind com.paulturner.nanorest to port, it's probably already used for something"
            );
            return;
        }

        ServerSession att = new ServerSession(socketChannel);
        socketChannel.accept(att, new ConnectionHandler());

        try {
            stopLatch.await();
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Server shutting down");
            stopLatch.countDown();
        }

    }
}