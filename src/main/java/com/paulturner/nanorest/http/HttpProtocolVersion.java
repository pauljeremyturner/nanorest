package com.paulturner.nanorest.http;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum HttpProtocolVersion {


    HTTP_1_1("HTTP/1.1"), HTTP_1_0("HTTP/1.0");

    static final Map<String, HttpProtocolVersion> LOOKUP_MAP = new HashMap<>();
    static final Map<HttpProtocolVersion, String> REVERSE_LOOKUP_MAP = new HashMap<>();


    static {
        Arrays.stream(HttpProtocolVersion.values()).forEach(
            hpv -> {
                LOOKUP_MAP.put(hpv.text, hpv);
                REVERSE_LOOKUP_MAP.put(hpv, hpv.text);
            }
        );
    }

    private final String text;
    private byte[] bytes;

    HttpProtocolVersion(String s) {
        this.text = s;
        this.bytes = text.getBytes(StandardCharsets.US_ASCII);
    }


    public String getText() {
        return text;
    }


    public byte[] toByteArray() {
        return bytes;
    }


}
