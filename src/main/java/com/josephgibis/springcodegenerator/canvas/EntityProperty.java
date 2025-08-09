package com.josephgibis.springcodegenerator.canvas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EntityProperty {
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty type = new SimpleStringProperty("");
    private final BooleanProperty nullable = new SimpleBooleanProperty(true);
    private final BooleanProperty unique = new SimpleBooleanProperty(false);
    private final BooleanProperty includeInCreateRequest = new SimpleBooleanProperty(true);
    private final BooleanProperty includeInUpdateRequest = new SimpleBooleanProperty(true);
    private final BooleanProperty includeInResponse = new SimpleBooleanProperty(true);

    public EntityProperty() {
    }

    public EntityProperty(String name, String type) {
        setName(name);
        setType(type);
    }

    public EntityProperty(String name, String type, boolean nullable, boolean unique, boolean includeInCreateRequest,
            boolean includeInUpdateRequest,
            boolean includeInResponse) {
        setName(name);
        setType(type);
        setNullable(nullable);
        setIncludeInCreateRequest(includeInCreateRequest);
        setIncludeInUpdateRequest(includeInUpdateRequest);
        setIncludeInResponse(includeInResponse);

    }

    @JsonProperty("name")
    public String getName() {
        return name.get();
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name.set(name);
    }

    @JsonProperty("type")
    public String getType() {
        return type.get();
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type.set(type);
    }

    @JsonProperty("nullable")
    public boolean isNullable() {
        return nullable.get();
    }

    @JsonProperty("nullable")
    public void setNullable(boolean nullable) {
        this.nullable.set(nullable);
    }

    @JsonProperty("unique")
    public boolean isUnique() {
        return unique.get();
    }

    @JsonProperty("unique")
    public void setUnique(boolean unique) {
        this.unique.set(unique);
    }

    @JsonProperty("includeInCreateRequest")
    public boolean isIncludeInCreateRequest() {
        return includeInCreateRequest.get();
    }

    @JsonProperty("includeInCreateRequest")
    public void setIncludeInCreateRequest(boolean includeInCreateRequest) {
        this.includeInCreateRequest.set(includeInCreateRequest);
    }

    @JsonProperty("includeInUpdateRequest")
    public boolean isIncludeInUpdateRequest() {
        return includeInUpdateRequest.get();
    }

    @JsonProperty("includeInUpdateRequest")
    public void setIncludeInUpdateRequest(boolean includeInUpdateRequest) {
        this.includeInUpdateRequest.set(includeInUpdateRequest);
    }

    @JsonProperty("includeInResponse")
    public boolean isIncludeInResponse() {
        return includeInResponse.get();
    }

    @JsonProperty("includeInResponse")
    public void setIncludeInResponse(boolean includeInResponse) {
        this.includeInResponse.set(includeInResponse);
    }

    @JsonIgnore
    public StringProperty nameProperty() {
        return name;
    }

    @JsonIgnore
    public StringProperty typeProperty() {
        return type;
    }

    @JsonIgnore
    public BooleanProperty nullableProperty() {
        return nullable;
    }

    @JsonIgnore
    public BooleanProperty uniqueProperty() {
        return unique;
    }

    @JsonIgnore
    public BooleanProperty includeInCreateRequestProperty() {
        return includeInCreateRequest;
    }

    @JsonIgnore
    public BooleanProperty includeInUpdateRequestProperty() {
        return includeInUpdateRequest;
    }

    @JsonIgnore
    public BooleanProperty includeInResponseProperty() {
        return includeInResponse;
    }
}