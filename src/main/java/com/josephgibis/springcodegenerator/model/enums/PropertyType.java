package com.josephgibis.springcodegenerator.model.enums;

import java.util.Arrays;

public enum PropertyType {
    STRING("String"),
    INTEGER("Integer"),
    LONG("Long"),
    DOUBLE("Double"),
    BOOLEAN("Boolean"),
    LOCAL_DATE("LocalDate"),
    LOCAL_DATE_TIME("LocalDateTime");

    private final String value;
    PropertyType(String value) { this.value = value; }
    public String getValue(){ return this.value; }

    public static String[] getStringValues() {
        return Arrays.stream(PropertyType.values())
                .map(PropertyType::getValue)
                .toArray(String[]::new);
    }
}
