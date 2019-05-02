package com.paulturner.nanorest.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulturner.nanorest.rest.RestEntity;
import com.paulturner.nanorest.rest.RestResource;

public class RequestRouter {

    private static final Logger logger = Logger.getLogger(RequestRouter.class.getName());

    private final Map<String, RestResource> uriResourceMap;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RequestRouter() {
        Map<String, RestResource> tmp = new HashMap<>();
        try (
            InputStream input = RequestRouter.class.getResourceAsStream("/httproutes");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(input))
        ) {
            buffer.lines().forEach(line -> {
                String[] tokens = line.split(" ");
                try {
                    tmp.put(tokens[0], (RestResource) Class.forName(tokens[1]).newInstance());
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    throw new IllegalStateException(("Could not find Resource class"));
                }
            });
        } catch (IOException ioe) {
            throw new IllegalStateException("Could not find httproutes file");
        }
        logger.log(Level.INFO, "Configured http routes [routes={0}]", tmp);
        uriResourceMap = Collections.unmodifiableMap(tmp);
    }

    public Response handleRequest(final Request httpRequest) {


        String path = httpRequest.getUri().getPath();
        Optional<RestResource> restResource = Optional.ofNullable(uriResourceMap.get(path));

        try {
            if (restResource.isPresent()) {
                final RestEntity restEntity = process(restResource.get(), httpRequest);
                final byte[] bytes = objectMapper.writeValueAsBytes(restEntity.getResponse());
                int contentLength = bytes.length;
                return Response.builder().withStatus(restEntity.getStatus()).withBody(bytes)
                    .withHttpHeaders(restEntity.getHttpHeaders().addHeader("Content-Length", Integer.toString(contentLength))).build();
            } else {
                return Responses.notFound();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            return Responses.internalError();
        }


    }


    private RestEntity process(RestResource restResource, Request httpRequest) {

        try {
            if (httpRequest.isGet()) {
                return restResource.doGet();
            } else if (httpRequest.isPost()) {
                return restResource.doPost(objectMapper.readValue(httpRequest.getBody(), restResource.getRequestType()));
            } else {
                return new RestEntity(Status.METHOD_NOT_ALLOWED, null, new Headers());
            }
        } catch (Exception e) {
            return new RestEntity(Status.INTERNAL_SERVER_ERROR, null, new Headers());
        }
    }

}
