package com.error.center.util.enums;

public enum Level {
    error("error"),
    warning("warning"),
    info("info");


    private final String value;

    Level(String value) {
        this.value = value;
    }

    public static Level getEnum(String value) {
        for (Level t : values()) {
            if (value.equals(t.getValue())) return t;
        }
        return null;
    }

    public String getValue() {
        return this.value;
    }
}
