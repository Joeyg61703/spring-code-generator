package com.josephgibis.springcodegenerator.canvas;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.josephgibis.springcodegenerator.canvas.enums.RelationshipType;

public class EntityRelationship {

    private CanvasEntity sourceEntity;
    private CanvasEntity targetEntity;
    private RelationshipType relationshipType;
    private String foreignKeyProperty;

    public EntityRelationship() {}

    public EntityRelationship(CanvasEntity sourceEntity, CanvasEntity targetEntity, RelationshipType type) {
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
        this.relationshipType = type;
        switch (type) {
            case ONE_TO_MANY, ONE_TO_ONE -> this.foreignKeyProperty = sourceEntity.getName().toLowerCase() + "_id";
            case MANY_TO_ONE -> this.foreignKeyProperty = targetEntity.getName().toLowerCase() + "_id";
            case MANY_TO_MANY -> this.foreignKeyProperty = null;
        }
    }

    public boolean containsEntity(CanvasEntity entity){
        return sourceEntity.equals(entity) || targetEntity.equals(entity);
    }

    public String compositeKey(){
        return sourceEntity.getName() + "->" + targetEntity.getName();
    }

    public RelationshipType getInverseRelationshipType(){
        RelationshipType inverse = null;
        switch (this.relationshipType){
            case ONE_TO_MANY -> inverse = RelationshipType.MANY_TO_ONE;
            case MANY_TO_ONE -> inverse = RelationshipType.ONE_TO_MANY;
            case MANY_TO_MANY -> inverse = RelationshipType.MANY_TO_MANY;
            case ONE_TO_ONE -> inverse = RelationshipType.ONE_TO_ONE;
        }
        return inverse;
    }

    @JsonProperty("sourceEntity")
    public CanvasEntity getSourceEntity() { return sourceEntity; }

    @JsonProperty("sourceEntity")
    public void setSourceEntity(CanvasEntity sourceEntity) { this.sourceEntity = sourceEntity; }

    @JsonProperty("targetEntity")
    public CanvasEntity getTargetEntity() { return targetEntity; }

    @JsonProperty("targetEntity")
    public void setTargetEntity(CanvasEntity targetEntity) { this.targetEntity = targetEntity; }

    @JsonProperty("relationshipType")
    public RelationshipType getRelationshipType() { return relationshipType; }

    @JsonProperty("relationshipType")
    public void setRelationshipType(RelationshipType relationshipType) { this.relationshipType = relationshipType; }

    @JsonProperty("foreignKeyProperty")
    public String getForeignKeyProperty() { return foreignKeyProperty; }

    @JsonProperty("foreignKeyProperty")
    public void setForeignKeyProperty(String foreignKeyProperty) { this.foreignKeyProperty = foreignKeyProperty; }

}