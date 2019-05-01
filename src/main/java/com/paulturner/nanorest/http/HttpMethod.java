package com.paulturner.nanorest.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum HttpMethod {

    GET, POST, INVALID;

    private static final Map<String, HttpMethod> LOOKUP_MAP = new HashMap<>();

    static {
        Arrays.stream(HttpMethod.values()).forEach(hm -> LOOKUP_MAP.put(hm.toString(), hm));
    }

    public static Optional<HttpMethod> lookup(String lookupValue) {
        return Optional.ofNullable(LOOKUP_MAP.get(lookupValue));
    }

}
