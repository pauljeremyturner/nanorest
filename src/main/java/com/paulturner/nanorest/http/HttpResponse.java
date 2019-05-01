package com.paulturner.nanorest.http;

import java.nio.ByteBuffer;
import java.util.Objects;

public class HttpResponse {


    private HttpStatus status;
    private HttpHeaders httpHeaders;
    private HttpProtocolVersion protocol = HttpProtocolVersion.HTTP_1_1;
    private byte[] body;

    private HttpResponse() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public HttpProtocolVersion getProtocol() {
        return protocol;
    }

    public byte[] getBody() {
        return body;
    }

    public void writeToBuffer(ByteBuffer byteBuffer) {
        byteBuffer.put(protocol.toByteArray());
        byteBuffer.put((byte) 32);
        byteBuffer.put(status.statusCodeByteArray());
        byteBuffer.put((byte) 32);
        byteBuffer.put(status.reasonByteArray());
        byteBuffer.put("\r\n".getBytes());
        byteBuffer.put(httpHeaders.toByteArray());
        byteBuffer.put("\r\n".getBytes());
        if (Objects.nonNull(body)) {
            byteBuffer.put(body);
        }

    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append(protocol.getText()).append(" ")
            .append(status.getStatusCode()).append(" ")
            .append(status.getReason()).append("\r\n")
            .append(httpHeaders.toString()).append("\r\n")
            .append(body == null ? "" : new String(body))
            .toString();
    }

    public static class Builder {

        private HttpStatus status;
        private HttpHeaders httpHeaders;
        private byte[] body;
        private HttpProtocolVersion protocol;

        public Builder withStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder withHttpHeaders(HttpHeaders httpHeaders) {
            this.httpHeaders = httpHeaders;
            return this;
        }

        public Builder withBody(byte[] body) {
            this.body = body;
            return this;
        }

        public Builder withProtocol(HttpProtocolVersion protocol) {
            this.protocol = protocol;
            return this;
        }

        public HttpResponse build() {
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.body = this.body;
            httpResponse.status = this.status;
            httpResponse.httpHeaders = this.httpHeaders;
            httpResponse.protocol = this.protocol;
            if (Objects.isNull(protocol)) {
                httpResponse.protocol = HttpProtocolVersion.HTTP_1_1;
            }
            return httpResponse;
        }

    }

}