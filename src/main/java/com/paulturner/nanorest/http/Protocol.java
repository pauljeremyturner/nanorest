package com.paulturner.nanorest.http;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Protocol {


    HTTP_1_1("HTTP/1.1"), HTTP_1_0("HTTP/1.0");

    static final Map<String, Protocol> LOOKUP_MAP = new HashMap<>();
    static final Map<Protocol, String> REVERSE_LOOKUP_MAP = new HashMap<>();


    static {
        Arrays.stream(Protocol.values()).forEach(
            hpv -> {
                LOOKUP_MAP.put(hpv.text, hpv);
                REVERSE_LOOKUP_MAP.put(hpv, hpv.text);
            }
        );
    }

    private final String text;
    private byte[] bytes;

    Protocol(String s) {
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
