package com.josephgibis.springcodegenerator.model.enums;

import java.util.Arrays;

public enum IdType {
        LONG("Long"),
        UUID("UUID"),
        INTEGER("Integer"),
        BIGINTEGER("BigInteger"),
        STRING("String");

        private final String value;
        IdType(String value) { this.value = value; }
        public String getValue(){ return this.value; }

        public static String[] getStringValues() {
            return Arrays.stream(IdType.values())
                    .map(IdType::getValue)
                    .toArray(String[]::new);
        }
}
