package com.paulturner.nanorest.http;


public class Responses {

    public static Response internalError() {
        return Response.builder().withStatus(Status.INTERNAL_SERVER_ERROR).build();
    }

    public static Response notFound() {
        return Response
            .builder()
            .withStatus(Status.NOT_FOUND)
            .withHttpHeaders(new Headers().addHeader("Content-Length", "0"))
            .build();
    }
}
