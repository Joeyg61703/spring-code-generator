package com.josephgibis.springcodegenerator.canvas;

import com.josephgibis.springcodegenerator.canvas.enums.RelationshipType;

public class EntityRelationship {

    private CanvasEntity sourceEntity;
    private CanvasEntity targetEntity;
    private RelationshipType relationshipType;
    private String foreignKeyProperty;

    public EntityRelationship(CanvasEntity sourceEntity, CanvasEntity targetEntity, RelationshipType type) {
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
        this.relationshipType = type;
        this.foreignKeyProperty = targetEntity.getName().toLowerCase() + "_id";
    }

    public boolean containsEntity(CanvasEntity entity){
        return sourceEntity.equals(entity) || targetEntity.equals(entity);
    }

    // Getters and setters
    public CanvasEntity getSourceEntity() { return sourceEntity; }
    public void setSourceEntity(CanvasEntity fromEntity) { this.sourceEntity = sourceEntity; }

    public CanvasEntity getTargetEntity() { return targetEntity; }
    public void setTargetEntity(CanvasEntity toEntity) { this.targetEntity = targetEntity; }

    public RelationshipType getRelationshipType() { return relationshipType; }
    public void setRelationshipType(RelationshipType relationshipType) { this.relationshipType = relationshipType; }

    public String getForeignKeyProperty() { return foreignKeyProperty; }
    public void setForeignKeyProperty(String foreignKeyField) { this.foreignKeyProperty = foreignKeyField; }
}