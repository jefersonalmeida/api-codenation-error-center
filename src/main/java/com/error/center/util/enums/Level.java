package com.error.center.util.enums;

public enum Level {
    ERROR("ERROR"),
    WARNING("WARNING"),
    INFO("INFO");

    private final String value;

    Level(String value) {
        this.value = value.toUpperCase();
    }

    public static Level getEnum(String value) {
        for (Level t : values()) {
            if (value.toUpperCase().equals(t.getValue().toUpperCase())) return t;
        }
        return null;
    }

    public String getValue() {
        return this.value.toUpperCase();
    }
}
