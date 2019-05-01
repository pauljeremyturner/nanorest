package com.paulturner.nanorest.http;

import java.nio.ByteBuffer;
import java.util.Objects;

public class Response {


    private Status status;
    private Headers httpHeaders;
    private Protocol protocol = Protocol.HTTP_1_1;
    private byte[] body;

    private Response() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Status getStatus() {
        return status;
    }

    public Headers getHttpHeaders() {
        return httpHeaders;
    }

    public Protocol getProtocol() {
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

        private Status status;
        private Headers httpHeaders;
        private byte[] body;
        private Protocol protocol;

        public Builder withStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder withHttpHeaders(Headers httpHeaders) {
            this.httpHeaders = httpHeaders;
            return this;
        }

        public Builder withBody(byte[] body) {
            this.body = body;
            return this;
        }

        public Builder withProtocol(Protocol protocol) {
            this.protocol = protocol;
            return this;
        }

        public Response build() {
            Response response = new Response();
            response.body = this.body;
            response.status = this.status;
            response.httpHeaders = this.httpHeaders;
            response.protocol = this.protocol;
            if (Objects.isNull(protocol)) {
                response.protocol = Protocol.HTTP_1_1;
            }
            return response;
        }

    }

}