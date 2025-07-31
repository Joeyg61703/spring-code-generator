package com.josephgibis.springcodegenerator.canvas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class CanvasState {

    private static final HashMap<String, CanvasEntity> entities = new HashMap<>();
    private static final ObservableList<EntityRelationship> relationships = FXCollections.observableArrayList();
    private static CanvasEntity selectedEntity;

    public static CanvasEntity getSelectedEntity(){
        return selectedEntity;
    }
    
    public static void setSelectedEntity(CanvasEntity entity){
        selectedEntity = entity;
    }

    public static HashMap<String, CanvasEntity> getEntityMap(){
        return entities;
    }

    public static void addEntity(CanvasEntity entity){
        entities.put(entity.getName(), entity);
    }

    public static void addRelationship(EntityRelationship relationship){
        relationships.add(relationship);
    }

    public static void removeEntityByName(String entityName){
        entities.remove(entityName);
        relationships.removeIf(relationship -> relationship.containsEntity((entities.get(entityName))));
    }

    public static void updateEntityByName(String name, CanvasEntity updatedEntity){
        String newName = updatedEntity.getName();
        if(newName.equals(name)){
            entities.put(name, updatedEntity);
            return;
        }
        removeEntityByName(name);
        addEntity(updatedEntity);


    }
}
