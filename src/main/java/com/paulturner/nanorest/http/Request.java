package com.paulturner.nanorest.http;

import java.net.URI;

public class Request {

    private static final String TOSTRING_MASK = "Request: [Method=%s] [URI=%s]";

    private Method method;
    private URI uri;
    private Protocol protocol;
    private byte[] body;
    private Headers httpHeaders = new Headers();

    private Request() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isGet() {
        return this.method == Method.GET;
    }

    public boolean isPost() {
        return this.method == Method.POST;
    }

    public Method getMethod() {
        return method;
    }

    public byte[] getBody() {
        return body;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return String.format(TOSTRING_MASK, method, uri);
    }

    public static final class Builder {
        private Method method;
        private URI uri;
        private Protocol protocol;
        private byte[] body;
        private Headers httpHeaders;

        public Builder withHttpMethod(Method method) {
            this.method = method;
            return this;
        }

        public Builder withUri(String uri) {
            this.uri = URI.create(uri);
            return this;
        }

        public Builder withProtocol(Protocol protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder withBody(byte[] body) {
            this.body = body;
            return this;
        }


        public Builder withNoBody() {
            this.body = new byte[0];
            return this;
        }

        public Builder withHttpHeaders(Headers httpHeaders) {
            this.httpHeaders = httpHeaders;
            return this;
        }

        public Request build() {
            Request request = new Request();
            request.body = this.body;
            request.method = this.method;
            request.uri = this.uri;
            request.httpHeaders = this.httpHeaders;
            request.protocol = this.protocol;
            return request;

        }
    }

}