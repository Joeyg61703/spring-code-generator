package com.josephgibis.springcodegenerator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.josephgibis.springcodegenerator.canvas.CanvasEntity;
import com.josephgibis.springcodegenerator.canvas.EntityRelationship;

import java.util.ArrayList;
import java.util.List;

public class ProjectData {

    @JsonProperty("entities")
    private List<CanvasEntity> entities = new ArrayList<>();

    @JsonProperty("relationships")
    private List<EntityRelationship> relationships = new ArrayList<>();

    public ProjectData(){}

    public ProjectData(List<CanvasEntity> entities, List<EntityRelationship> relationships) {
        this.entities = entities != null ? entities : new ArrayList<>();
        this.relationships = relationships != null ? relationships : new ArrayList<>();
    }

    public List<CanvasEntity> getEntities() { return entities; }
    public void setEntities(List<CanvasEntity> entities) {
        this.entities = entities != null ? entities : new ArrayList<>();
    }

    public List<EntityRelationship> getRelationships() { return relationships; }
    public void setRelationships(List<EntityRelationship> relationships) {
        this.relationships = relationships != null ? relationships : new ArrayList<>();
    }

}
