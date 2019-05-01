package com.paulturner.nanorest.http;

import java.net.URI;

public class HttpRequest {

    private static final String TOSTRING_MASK = "HttpRequest: [HttpMethod=%s] [URI=%s]";

    private HttpMethod httpMethod;
    private URI uri;
    private HttpProtocolVersion protocol;
    private byte[] body;
    private HttpHeaders httpHeaders = new HttpHeaders();

    private HttpRequest() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isGet() {
        return this.httpMethod == HttpMethod.GET;
    }

    public boolean isPost() {
        return this.httpMethod == HttpMethod.POST;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public byte[] getBody() {
        return body;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return String.format(TOSTRING_MASK, httpMethod, uri);
    }

    public static final class Builder {
        private HttpMethod httpMethod;
        private URI uri;
        private HttpProtocolVersion protocol;
        private byte[] body;
        private HttpHeaders httpHeaders;

        public Builder withHttpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder withUri(String uri) {
            this.uri = URI.create(uri);
            return this;
        }

        public Builder withProtocol(HttpProtocolVersion protocol) {
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

        public Builder withHttpHeaders(HttpHeaders httpHeaders) {
            this.httpHeaders = httpHeaders;
            return this;
        }

        public HttpRequest build() {
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.body = this.body;
            httpRequest.httpMethod = this.httpMethod;
            httpRequest.uri = this.uri;
            httpRequest.httpHeaders = this.httpHeaders;
            httpRequest.protocol = this.protocol;
            return httpRequest;

        }
    }

}