package com.paulturner.nanorest.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Method {

    GET, POST, INVALID;

    private static final Map<String, Method> LOOKUP_MAP = new HashMap<>();

    static {
        Arrays.stream(Method.values()).forEach(hm -> LOOKUP_MAP.put(hm.toString(), hm));
    }

    public static Optional<Method> lookup(String lookupValue) {
        return Optional.ofNullable(LOOKUP_MAP.get(lookupValue));
    }

}
