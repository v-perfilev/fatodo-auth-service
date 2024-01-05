package com.persoff68.fatodo.config.constant;

import java.util.Arrays;

public enum Provider {
    LOCAL(Constants.LOCAL_VALUE),
    GOOGLE(Constants.GOOGLE_VALUE),
    APPLE(Constants.APPLE_VALUE);

    private final String value;

    Provider(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean contains(String value) {
        return Arrays.stream(values()).anyMatch(provider -> provider.getValue().equals(value));
    }

    public static class Constants {
        public static final String LOCAL_VALUE = "LOCAL";
        public static final String GOOGLE_VALUE = "GOOGLE";
        public static final String APPLE_VALUE = "APPLE";

        private Constants() {
        }
    }
}
