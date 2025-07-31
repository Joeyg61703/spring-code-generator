package com.josephgibis.springcodegenerator.canvas;

import com.josephgibis.springcodegenerator.canvas.enums.RelationshipType;
import com.josephgibis.springcodegenerator.canvas.visuals.EntityVBox;
import com.josephgibis.springcodegenerator.canvas.visuals.RelationshipLine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanvasManager {
    private static final Map<String, EntityVBox> entityVBoxMap = new HashMap<>();

    private static final ObservableList<EntityRelationship> relationships = FXCollections.observableArrayList();
    private static final ObservableList<RelationshipLine> relationshipLines = FXCollections.observableArrayList();
    private static CanvasEntity selectedEntity;
    private static Pane canvas;
    private static Text selectedEntityHeader;

    public static void setCanvas(Pane canvasPane) {
        canvas = canvasPane;
    }
    public static void setSelectedEntityHeader(Text header){
        selectedEntityHeader = header;
    }

    private static void addEntityVbox(String entityName, EntityVBox entityVBox) {
        entityVBoxMap.put(entityName, entityVBox);
    }

    private static void removeEntityVbox(String entityName) {
        entityVBoxMap.remove(entityName);
    }

    public static Map<String, EntityVBox> getEntityVBoxMap(){
        return entityVBoxMap;
    }

    public static EntityVBox getEntityVboxByName(String entityName){
        return entityVBoxMap.get(entityName);
    }

    public static CanvasEntity getCanvasEntityFromName(String entityName){
        return entityVBoxMap.get(entityName).getEntity();
    }

    // ==============================
    //          ENTITIES
    // ==============================

    public static CanvasEntity getSelectedEntity(){
        return selectedEntity;
    }
    
    public static void setSelectedEntity(CanvasEntity entity){
        selectedEntity = entity;
        selectedEntityHeader.setText("Selected Entity: " + (entity != null ? entity.getName() : "None"));
    }

    public static void addEntity(CanvasEntity entity){
        EntityVBox entityVBox = new EntityVBox(entity, canvas);
        entityVBox.setOnMouseClicked(ignore -> selectedEntity = entity);
        entityVBoxMap.put(entity.getName(), entityVBox);
        canvas.getChildren().add(entityVBox);
    }

    public static void removeEntity(String entityName){
        CanvasEntity canvasEntity = getCanvasEntityFromName(entityName);
        removeEntityVbox(entityName);

        canvas.getChildren().removeIf(child -> {
            if(child instanceof EntityVBox){
                return ((EntityVBox)child).getEntity().getName().equals(entityName);
            }
            if(child instanceof RelationshipLine){
                return ((RelationshipLine)child).getRelationship().containsEntity(canvasEntity);
            }
            return false;
        });
        relationships.removeIf(relationship -> relationship.containsEntity(canvasEntity));
        relationshipLines.removeIf(line -> line.getRelationship().containsEntity(canvasEntity));

    }

    public static void updateEntityByName(String originalName, CanvasEntity updatedEntity){
        String newName = updatedEntity.getName();
        if(newName.equals(originalName)){
            EntityVBox originalVbox = entityVBoxMap.get(originalName);
            originalVbox.setEntity(updatedEntity);
            originalVbox.updateDisplay();
            return;
        }

        EntityVBox entityVBox = entityVBoxMap.remove(originalName);
        entityVBox.setEntity(updatedEntity);
        entityVBox.updateDisplay();
        entityVBoxMap.put(newName, entityVBox);

    }

    public static List<String> getEntityNameList(){
        return new ArrayList<>(entityVBoxMap.keySet());
    }

    // ==============================
    //          RELATIONSHIPS
    // ==============================

    public static ObservableList<EntityRelationship> getRelationships() {
        return relationships;
    }

    public static void addRelationship(EntityRelationship relationship){
        relationships.add(relationship);
    }

    public static void addRelationshipLine(RelationshipLine relationshipLine) {
        relationshipLines.add(relationshipLine);
        canvas.getChildren().add(0, relationshipLine);
    }

    public static void removeRelationshipLine(RelationshipLine relationshipLine) {
        relationshipLines.remove(relationshipLine);
    }

    public static ObservableList<RelationshipLine> getRelationshipLines() {
        return relationshipLines;
    }

    public static void createRelationshipByNames(String sourceEntityName, String targetEntityName, RelationshipType relationshipType){
        EntityVBox sourceEntityVbox = getEntityVboxByName(sourceEntityName);
        EntityVBox targetEntityVbox = getEntityVboxByName(targetEntityName);

        CanvasEntity sourceEntity = getCanvasEntityFromName(sourceEntityName);
        CanvasEntity targetEntity = getCanvasEntityFromName(targetEntityName);

        EntityRelationship relationship = new EntityRelationship(sourceEntity, targetEntity, relationshipType);
        RelationshipLine line = new RelationshipLine(relationship, sourceEntityVbox, targetEntityVbox);
        addRelationship(relationship);
        addRelationshipLine(line);

    }

}
