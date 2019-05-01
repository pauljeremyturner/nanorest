package com.paulturner.nanorest.http;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class HttpHeaders {

    private Map<String, String> headers = new LinkedHashMap<>();

    public HttpHeaders() {
    }

    public HttpHeaders addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        headers.entrySet().forEach(
            entry ->
                stringBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n")
        );
        return stringBuilder.toString();
    }

    public byte[] toByteArray() {
        return this.toString().getBytes();
    }

    public Optional<String> getHeader(String header) {
        return Optional.ofNullable(headers.get(header));
    }

}
