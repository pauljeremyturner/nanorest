package com.paulturner.nanorest.http;


public class HttpResponses {

    static HttpResponse internalError() {
        return HttpResponse.builder().withStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public static HttpResponse notFound() {
        return HttpResponse
            .builder()
            .withStatus(HttpStatus.NOT_FOUND)
            .withHttpHeaders(new HttpHeaders().addHeader("Content-Length", "0"))
            .build();
    }
}
