package com.paulturner.nanorest.rest;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulturner.nanorest.http.Headers;
import com.paulturner.nanorest.http.Request;
import com.paulturner.nanorest.http.Response;
import com.paulturner.nanorest.http.Responses;
import com.paulturner.nanorest.http.Status;
import com.paulturner.nanorest.nio.ByteBufferPool;

public class RequestRouter {

    private static final Logger logger = Logger.getLogger(RequestRouter.class.getName());

    private final Map<String, RestResource> uriResourceMap;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static final RequestRouter instance = new RequestRouter();

    public static final RequestRouter  getInstance() {
        return instance;
    }


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

    public Response handleRequest(final Request request) {


        String path = request.getUri().getPath();
        Optional<RestResource> restResource = Optional.ofNullable(uriResourceMap.get(path));

        try {
            if (restResource.isPresent()) {
                final RestEntity restEntity = process(restResource.get(), request);
                final byte[] bytes;
                int contentLength;
                if (restEntity.getStatus().is2xx()) {
                    bytes = objectMapper.writeValueAsBytes(restEntity.getResponse());
                    contentLength = bytes.length;
                } else {
                    bytes = new byte[0];
                    contentLength = 0;
                }
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


    private RestEntity process(RestResource restResource, Request request) {

        try {
            if (request.isGet()) {
                return restResource.doGet();
            } else if (request.isPost()) {
                return restResource.doPost(objectMapper.readValue(request.getBody(), restResource.getRequestType()));
            } else {
                return new RestEntity(Status.METHOD_NOT_ALLOWED, null, new Headers());
            }
        } catch (JsonParseException | JsonMappingException je) {
            return new RestEntity(Status.BAD_REQUEST, null, new Headers());
        } catch (Exception e) {
            return new RestEntity(Status.INTERNAL_SERVER_ERROR, null, new Headers());
        }
    }

}
