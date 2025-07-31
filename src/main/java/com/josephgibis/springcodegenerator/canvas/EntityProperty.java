package com.josephgibis.springcodegenerator.canvas;

import javafx.beans.property.*;

public class EntityProperty {
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty type = new SimpleStringProperty("String");
    private final BooleanProperty nullable = new SimpleBooleanProperty(true);
    private final BooleanProperty unique = new SimpleBooleanProperty(false);

    public EntityProperty() {}

    public EntityProperty(String name, String type) {
        setName(name);
        setType(type);
    }

    public EntityProperty(String name, String type, boolean nullable, boolean unique){
        setName(name);
        setType(type);
        setNullable(nullable);
        setUnique(unique);
    }

    // Getters and setters
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
}