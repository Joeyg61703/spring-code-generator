package com.josephgibis.springcodegenerator.canvas;

import com.josephgibis.springcodegenerator.canvas.enums.RelationshipType;
import com.josephgibis.springcodegenerator.canvas.visuals.EntityVBox;
import com.josephgibis.springcodegenerator.canvas.visuals.RelationshipLine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.*;

public class CanvasManager {
    private static final Map<String, EntityVBox> entityVBoxMap = new HashMap<>();

    private static final ObservableList<EntityRelationship> relationships = FXCollections.observableArrayList();
    private static final ObservableList<RelationshipLine> relationshipLines = FXCollections.observableArrayList();
    private static CanvasEntity selectedEntity;
    private static EntityRelationship selectedRelationship;

    private static Pane canvas;

    private static Text selectedEntityHeader;
    private static Text selectedRelationshipHeader;


    public static void setCanvas(Pane canvasPane) {
        canvas = canvasPane;
    }
    public static void setSelectedEntityHeader(Text header){
        selectedEntityHeader = header;
    }
    public static void setSelectedRelationshipHeader(Text header) {
        selectedRelationshipHeader = header;
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
        if(selectedEntity != null){
            EntityVBox previousSelectedEntityVbox = getEntityVboxByName(selectedEntity.getName());
            previousSelectedEntityVbox.getStyleClass().remove("selected-entity");
        }

        selectedEntity = entity;
        if(entity != null) {
            EntityVBox selectedEntityVbox = getEntityVboxByName(entity.getName());
            selectedEntityVbox.getStyleClass().add("selected-entity");
        }
        selectedEntityHeader.setText("Selected Entity: " + (entity != null ? entity.getName() : "None"));
    }

    public static void addEntity(CanvasEntity entity){
        EntityVBox entityVBox = new EntityVBox(entity, canvas);
        entityVBoxMap.put(entity.getName(), entityVBox);
        canvas.getChildren().add(entityVBox);
        setSelectedEntity(entity);
    }

    public static void removeEntity(String entityName){
        CanvasEntity canvasEntity = getCanvasEntityFromName(entityName);
        removeEntityVbox(entityName);
        setSelectedRelationship(null);

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

    public static EntityRelationship getSelectedRelationship(){
        return selectedRelationship;
    }

    public static void setSelectedRelationship(EntityRelationship relationship){
        if(selectedRelationship != null){
            Optional<RelationshipLine> previousRelationshipLine = getRelationshipLineFromRelationship(selectedRelationship);
            previousRelationshipLine.ifPresent(relationshipLine -> relationshipLine.getStyleClass().remove("selected-relationship"));
        }

        selectedRelationship = relationship;
        if(relationship != null) {
            Optional<RelationshipLine> selectedRelationshipLine = getRelationshipLineFromRelationship(relationship);
            selectedRelationshipLine.ifPresent(relationshipLine -> relationshipLine.getStyleClass().add("selected-relationship"));
        }
        selectedRelationshipHeader.setText("Selected Relationship: " + (relationship != null ? relationship.compositeKey() : "None"));
    }

    public static ObservableList<EntityRelationship> getRelationships() {
        return relationships;
    }

    public static Optional<RelationshipLine> getRelationshipLineFromRelationship(EntityRelationship relationship){
        return relationshipLines
                .stream()
                .filter(line -> line.getRelationship().equals(relationship))
                .findFirst();
    }

    public static void addRelationship(EntityRelationship relationship){
        relationships.add(relationship);
    }

    public static void addRelationshipLine(RelationshipLine relationshipLine) {
        relationshipLines.add(relationshipLine);
        canvas.getChildren().add(0, relationshipLine);
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

    public static void removeRelationship(EntityRelationship relationship) {
        setSelectedRelationship(null);
        relationships.remove(relationship);
        List<RelationshipLine> linesToRemove = new ArrayList<>(relationshipLines
                .stream()
                .filter(line -> line.getRelationship().equals(relationship))
                .toList());

        relationshipLines.removeAll(linesToRemove);
        canvas.getChildren().removeAll(linesToRemove);
    }

    public static void updateRelationships(List<EntityRelationship> updatedRelationships) {
        setSelectedRelationship(null);
        clearRelationshipLines();
        relationships.clear();
        relationships.addAll(updatedRelationships);
        redrawRelationshipLines();
    }

    public static void clearRelationshipLines(){
        canvas.getChildren().removeIf(child -> child instanceof RelationshipLine);
        relationshipLines.clear();
    }

    public static void redrawRelationshipLines(){
        for (EntityRelationship relationship : relationships) {
            EntityVBox sourceVBox = getEntityVboxByName(relationship.getSourceEntity().getName());
            EntityVBox targetVBox = getEntityVboxByName(relationship.getTargetEntity().getName());

            RelationshipLine line = new RelationshipLine(relationship, sourceVBox, targetVBox);
            addRelationshipLine(line);
        }
    }


}
