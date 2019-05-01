package com.paulturner.nanorest.http;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum HttpStatus {

    OK(200, "OK"),

    BAD_REQUEST(400, "Bad HttpRequest"),

    NOT_FOUND(404, "Not Found"),

    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    static final Map<Integer, HttpStatus> LOOKUP = new HashMap<>();

    static {
        Arrays.stream(HttpStatus.values()).forEach(hs -> LOOKUP.put(hs.getStatusCode(), hs));
    }

    private final int statusCode;
    private final String reason;
    private byte[] statusBytes;
    private byte[] reasonBytes;

    HttpStatus(final int statusCode, final String reason) {
        this.statusCode = statusCode;
        this.reason = reason;
        this.statusBytes = Integer.toString(statusCode).getBytes();
        this.reasonBytes = reason.getBytes(StandardCharsets.US_ASCII);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] statusCodeByteArray() {
        return statusBytes;
    }

    public byte[] reasonByteArray() {
        return statusBytes;
    }


    public String getReason() {
        return reason;
    }
}