package com.josephgibis.springcodegenerator.canvas.enums;

public enum RelationshipType {
    ONE_TO_ONE("@OneToOne"),
    ONE_TO_MANY("@OneToMany"),
    MANY_TO_ONE("@ManyToOne"),
    MANY_TO_MANY("@ManyToMany");

    private final String annotation;
    RelationshipType(String annotation) { this.annotation = annotation; }
    public String getAnnotation() { return annotation; }
}