package com.chewy;//package example;

import com.google.common.collect.ImmutableMap;

/**
 * Enumeration of the contact channel notifications will be sent on.
 */
public enum Channel {
    PUSH("PUSH"),
    EMAIL("EMAIL"),
    FAX("FAX");

    private static final ImmutableMap<String, Channel> ENUM_MAP;

    private String value;

    Channel(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    static {
        ENUM_MAP = new ImmutableMap.Builder<String, Channel>()
                .put(PUSH.value, PUSH)
                .put(EMAIL.value, EMAIL)
                .put(FAX.value, FAX)
                .build();
    }

    public static Channel channelFor(String channel) {
        return ENUM_MAP.get(channel);
    }
}