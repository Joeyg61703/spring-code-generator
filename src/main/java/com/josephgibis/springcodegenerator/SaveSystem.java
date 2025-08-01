package com.josephgibis.springcodegenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.josephgibis.springcodegenerator.canvas.CanvasEntity;
import com.josephgibis.springcodegenerator.canvas.CanvasManager;
import com.josephgibis.springcodegenerator.canvas.EntityRelationship;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaveSystem {

    public ObjectMapper objectMapper;

    public SaveSystem(){
        this.objectMapper = new ObjectMapper();

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public ProjectData loadSaveFromFile(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("File not found: " + file.getAbsolutePath());
        }

        try {
            return objectMapper.readValue(file, ProjectData.class);
        } catch (IOException e) {
            throw new IOException("Failed to load project from file: " + file.getName(), e);
        }
    }

    public void saveToFile(File file, ProjectData projectData) throws IOException {
        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            objectMapper.writeValue(file, projectData);
        } catch (IOException e) {
            throw new IOException("Failed to save project to file: " + file.getName(), e);
        }
    }

    public void saveCanvasToFile(File file) throws IOException {
        ProjectData projectData = createProjectDataFromCanvas();
        saveToFile(file, projectData);
    }

    public void loadFileToCanvas(File file) throws IOException {
        ProjectData projectData = loadSaveFromFile(file);
        loadProjectDataIntoCanvas(projectData);
    }

    public ProjectData createProjectDataFromCanvas() {
        List<CanvasEntity> entities = new ArrayList<>();

        CanvasManager.getEntityVBoxMap().forEach((name, entityVBox) -> {
            if (entityVBox.getEntity() != null) {
                entities.add(entityVBox.getEntity());
            }
        });

        List<EntityRelationship> relationships = new ArrayList<>(CanvasManager.getRelationships());

        return new ProjectData(entities, relationships);
    }

    public void loadProjectDataIntoCanvas(ProjectData projectData) {
        CanvasManager.clearCanvas();

        for (CanvasEntity entity : projectData.getEntities()) {
            CanvasManager.addEntity(entity);

            if (entity.getX() != 0 || entity.getY() != 0) {
                var entityVBox = CanvasManager.getEntityVboxByName(entity.getName());
                if (entityVBox != null) {
                    entityVBox.setLayoutX(entity.getX());
                    entityVBox.setLayoutY(entity.getY());
                }
            }
        }

        for (EntityRelationship relationship : projectData.getRelationships()) {
            CanvasManager.createRelationshipByNames(
                    relationship.getSourceEntity().getName(),
                    relationship.getTargetEntity().getName(),
                    relationship.getRelationshipType()
            );
        }
    }

}
