package com.paulturner.nanorest.http;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Headers {

    private static final String HEADER_CONTENT_LENGTH = "content-length";

    private Map<String, String> headers = new LinkedHashMap<>();

    public Headers() {
    }

    public Headers addHeader(String key, String value) {
        headers.put(key.toLowerCase(), value);
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

    public Optional<Integer> getContentLength() {
        final String s = headers.get(HEADER_CONTENT_LENGTH);
        Integer result = null;
        if (Objects.isNull(s) || s.length() == 0) {
            result = Integer.parseInt(s);
        }
        return Optional.ofNullable(result);
    }

}
