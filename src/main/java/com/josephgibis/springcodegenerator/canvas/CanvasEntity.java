package com.josephgibis.springcodegenerator.canvas;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CanvasEntity {
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty tableName = new SimpleStringProperty("");
    private final ObservableList<EntityProperty> properties = FXCollections.observableArrayList();
    private final StringProperty idType = new SimpleStringProperty("Long");

    private double x, y;

    public CanvasEntity(String name) {
        setName(name);
        setTableName(name.toLowerCase() + "s");

        //defaults
        addProperty(new EntityProperty("name", "String"));
        addProperty(new EntityProperty("createdAt", "LocalDateTime"));
        addProperty(new EntityProperty("updatedAt", "LocalDateTime"));
    }

    public CanvasEntity(CanvasEntity entity){
        setName(entity.getName());
        setTableName(entity.getTableName());
        setIdType(entity.getIdType());

        setX(entity.getX());
        setY(entity.getY());
    }

    // Getters and setters
    public StringProperty nameProperty() { return name; }
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public StringProperty tableNameProperty() { return tableName; }
    public String getTableName() { return tableName.get(); }
    public void setTableName(String tableName) { this.tableName.set(tableName); }

    public ObservableList<EntityProperty> getProperties() { return properties; }
    public void addProperty(EntityProperty property) { properties.add(property); }

    public StringProperty idTypeProperty() { return idType; }
    public String getIdType() { return idType.get(); }
    public void setIdType(String idType) { this.idType.set(idType); }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
}