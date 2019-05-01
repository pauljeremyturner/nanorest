package com.paulturner.nanorest;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.paulturner.nanorest.configuration.ApplicationConfiguration;

public class ServerMain {

    private static final Logger logger = Logger.getLogger(ServerMain.class.getName());

    public static void main(final String[] args) {

        if (args.length > 0) {
            logger.log(Level.INFO, "Loading configuration from {0}", args[0]);
            ApplicationConfiguration.loadFromPath(args[0]);
        }

        final Server server = new Server();
        newSingleThreadExecutor().execute(server);
    }
}