package com.josephgibis.springcodegenerator.canvas.enums;

import java.util.Arrays;

public enum IdGenerationType {
    IDENTITY("IDENTITY"),
    UUID("UUID"),
    SEQUENCE("SEQUENCE"),
    TABLE("TABLE"),
    AUTO("AUTO"),
    NONE("NONE");

    private final String value;
    IdGenerationType(String value) { this.value = value; }
    public String getValue(){ return this.value; }

    public static String[] getStringValues() {
        return Arrays.stream(IdGenerationType.values())
                .map(IdGenerationType::getValue)
                .toArray(String[]::new);
    }
}


