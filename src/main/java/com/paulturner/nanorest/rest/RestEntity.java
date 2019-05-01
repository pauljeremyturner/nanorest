package com.paulturner.nanorest.rest;

import com.paulturner.nanorest.http.Headers;
import com.paulturner.nanorest.http.Status;

public class RestEntity<T> {

    private Status status;
    private T response;
    private Headers httpHeaders;

    public RestEntity(Status status, T response, Headers httpHeaders) {
        this.status = status;
        this.response = response;
        this.httpHeaders = httpHeaders;
    }

    public Status getStatus() {
        return status;
    }

    public T getResponse() {
        return response;
    }

    public Headers getHttpHeaders() {
        return httpHeaders;
    }
}
