package com.josephgibis.springcodegenerator.canvas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.*;

public class EntityProperty {
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty type = new SimpleStringProperty("");
    private final BooleanProperty nullable = new SimpleBooleanProperty(true);
    private final BooleanProperty unique = new SimpleBooleanProperty(false);

    public EntityProperty() {}

    public EntityProperty(String name, String type) {
        setName(name);
        setType(type);
    }

    public EntityProperty(String name, String type, boolean nullable, boolean unique) {
        setName(name);
        setType(type);
        setNullable(nullable);
        setUnique(unique);
    }

    @JsonProperty("name")
    public String getName() { return name.get(); }

    @JsonProperty("name")
    public void setName(String name) { this.name.set(name); }

    @JsonProperty("type")
    public String getType() { return type.get(); }

    @JsonProperty("type")
    public void setType(String type) { this.type.set(type); }

    @JsonProperty("nullable")
    public boolean isNullable() { return nullable.get(); }

    @JsonProperty("nullable")
    public void setNullable(boolean nullable) { this.nullable.set(nullable); }

    @JsonProperty("unique")
    public boolean isUnique() { return unique.get(); }

    @JsonProperty("unique")
    public void setUnique(boolean unique) { this.unique.set(unique); }

    @JsonIgnore
    public StringProperty nameProperty() { return name; }

    @JsonIgnore
    public StringProperty typeProperty() { return type; }

    @JsonIgnore
    public BooleanProperty nullableProperty() { return nullable; }

    @JsonIgnore
    public BooleanProperty uniqueProperty() { return unique; }
}