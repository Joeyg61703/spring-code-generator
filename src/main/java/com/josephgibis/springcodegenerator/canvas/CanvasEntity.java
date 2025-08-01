package com.josephgibis.springcodegenerator.canvas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class CanvasEntity {
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty tableName = new SimpleStringProperty("");
    private final ObservableList<EntityProperty> properties = FXCollections.observableArrayList();
    private final StringProperty idType = new SimpleStringProperty("Long");

    private double x, y;

    public CanvasEntity() {}

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

    @JsonProperty("name")
    public String getName() { return name.get(); }

    @JsonProperty("name")
    public void setName(String name) { this.name.set(name); }

    @JsonProperty("tableName")
    public String getTableName() { return tableName.get(); }

    @JsonProperty("tableName")
    public void setTableName(String tableName) { this.tableName.set(tableName); }

    @JsonProperty("idType")
    public String getIdType() { return idType.get(); }

    @JsonProperty("idType")
    public void setIdType(String idType) { this.idType.set(idType); }

    @JsonProperty("x")
    public double getX() { return x; }

    @JsonProperty("x")
    public void setX(double x) { this.x = x; }

    @JsonProperty("y")
    public double getY() { return y; }

    @JsonProperty("y")
    public void setY(double y) { this.y = y; }

    @JsonProperty("properties")
    public List<EntityProperty> getPropertiesList() {
        return new ArrayList<>(properties);
    }

    @JsonProperty("properties")
    public void setPropertiesList(List<EntityProperty> propertiesList) {
        properties.clear();
        if (propertiesList != null) {
            properties.addAll(propertiesList);
        }
    }

    @JsonIgnore
    public StringProperty nameProperty() { return name; }

    @JsonIgnore
    public StringProperty tableNameProperty() { return tableName; }

    @JsonIgnore
    public StringProperty idTypeProperty() { return idType; }

    @JsonIgnore
    public ObservableList<EntityProperty> getProperties() { return properties; }

    public void addProperty(EntityProperty property) { properties.add(property); }
}