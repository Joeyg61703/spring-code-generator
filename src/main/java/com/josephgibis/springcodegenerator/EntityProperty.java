package com.josephgibis.springcodegenerator;

import javafx.beans.property.*;

public class EntityProperty {
    private final StringProperty name;
    private final StringProperty type;
    private final BooleanProperty nullable;
    private final BooleanProperty unique;
    private final StringProperty defaultValue;

    public EntityProperty() {
        this.name = new SimpleStringProperty("name");
        this.type = new SimpleStringProperty("String");
        this.nullable = new SimpleBooleanProperty(true);
        this.unique = new SimpleBooleanProperty(false);
        this.defaultValue = new SimpleStringProperty("");
    }

    public EntityProperty(String name, String type, boolean nullable, boolean unique, String defaultValue) {
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.nullable = new SimpleBooleanProperty(nullable);
        this.unique = new SimpleBooleanProperty(unique);
        this.defaultValue = new SimpleStringProperty(defaultValue);
    }

    public StringProperty nameProperty() { return name; }
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public StringProperty typeProperty() { return type; }
    public String getType() { return type.get(); }
    public void setType(String type) { this.type.set(type); }

    public BooleanProperty nullableProperty() { return nullable; }
    public boolean isNullable() { return nullable.get(); }
    public void setNullable(boolean nullable) { this.nullable.set(nullable); }

    public BooleanProperty uniqueProperty() { return unique; }
    public boolean isUnique() { return unique.get(); }
    public void setUnique(boolean unique) { this.unique.set(unique); }

    public StringProperty defaultValueProperty() { return defaultValue; }
    public String getDefaultValue() { return defaultValue.get(); }
    public void setDefaultValue(String defaultValue) { this.defaultValue.set(defaultValue); }
}