package com.paulturner.nanorest.http;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Status {

    OK(200, "OK"),

    BAD_REQUEST(400, "Bad Request"),

    NOT_FOUND(404, "Not Found"),

    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    static final Map<Integer, Status> LOOKUP = new HashMap<>();

    static {
        Arrays.stream(Status.values()).forEach(hs -> LOOKUP.put(hs.getStatusCode(), hs));
    }

    private final int statusCode;
    private final String reason;
    private byte[] statusBytes;
    private byte[] reasonBytes;

    Status(final int statusCode, final String reason) {
        this.statusCode = statusCode;
        this.reason = reason;
        this.statusBytes = Integer.toString(statusCode).getBytes(StandardCharsets.US_ASCII);
        this.reasonBytes = reason.getBytes(StandardCharsets.US_ASCII);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] statusCodeByteArray() {
        return statusBytes;
    }

    public byte[] reasonByteArray() {
        return reasonBytes;
    }


    public String getReason() {
        return reason;
    }

    public boolean is2xx() {
        return statusCode >=200 && statusCode < 300;
    }
}