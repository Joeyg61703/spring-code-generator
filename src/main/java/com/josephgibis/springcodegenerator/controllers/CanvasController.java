package com.josephgibis.springcodegenerator.controllers;

import com.josephgibis.springcodegenerator.canvas.*;
import com.josephgibis.springcodegenerator.canvas.EntityProperty;
import com.josephgibis.springcodegenerator.canvas.enums.RelationshipType;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CanvasController implements Initializable {

    @FXML public Text selectedEntityHeader;
    @FXML private Pane canvas;

    private String componentsDir = "/com/josephgibis/springcodegenerator/components/";

    //TODO: prevent duplicate names

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        canvas.setOnMouseClicked(this::handleCanvasClick);
    }

    @FXML
    private void addNewEntity() {

        Optional<String> entityNameInput = getEntityNameFromDialog();
        if(entityNameInput.isEmpty()) return;

        String entityName = entityNameInput.get().trim();

        if(entityName.isEmpty()){
            showAlert("Failed to Create Entity", "Cannot create entity without a name.");
            return;
        }

        CanvasEntity newCanvasEntity = new CanvasEntity(entityName);
        CanvasState.addEntity(newCanvasEntity);

        EntityVBox entityVBox = new EntityVBox(newCanvasEntity, canvas);
        entityVBox.setOnMouseClicked(event -> selectEntity(newCanvasEntity));

        canvas.getChildren().add(entityVBox);

        selectEntity(newCanvasEntity);
    }

    @FXML
    private void addRelationship() {
        HashMap<String, CanvasEntity> entities = CanvasState.getEntityMap();
        if (entities.size() < 2) {
            showAlert("Need Entities", "You need at least 2 entities to create a relationship.");
            return;
        }

        Optional<String> sourceDialogInput = getRelationshipSourceEntityFromDialog();
        if(sourceDialogInput.isEmpty()) return;
        String sourceEntityName = sourceDialogInput.get();

        Optional<String> targetDialogInput = getRelationshipTargetEntityFromDialog(sourceEntityName);
        if(targetDialogInput.isEmpty()) return;
        String targetEntityName = targetDialogInput.get();

        Optional<RelationshipType> relationTypeInput = getRelationshipTypeFromDialog();
        if(relationTypeInput.isEmpty()) return;
        RelationshipType relationType = relationTypeInput.get();

        CanvasEntity sourceEntity = entities.get(sourceEntityName);
        CanvasEntity targetEntity = entities.get(targetEntityName);

        EntityRelationship relationship = new EntityRelationship(sourceEntity, targetEntity, relationType);
        CanvasState.addRelationship(relationship);

        if (    relationType == RelationshipType.MANY_TO_ONE ||
                relationType == RelationshipType.ONE_TO_ONE  ||
                relationType == RelationshipType.ONE_TO_MANY
        ) {

            targetEntity.addProperty(new EntityProperty(
                    relationship.getForeignKeyProperty(),
                    sourceEntity.getIdType()
            ));
            updateEntityDisplay(targetEntity);
        }

        //TODO: create table for many to many?

        showAlert("Relationship Created",
                String.format("Created %s relationship from %s to %s",
                relationType, sourceEntity.getName(), targetEntity.getName()));

    }

    @FXML
    private void generateDesignCode() {
        HashMap<String, CanvasEntity> entities = CanvasState.getEntityMap();
        if (entities.isEmpty()) {
            showAlert("No Entities", "No Entities to generate.");
            return;
        }
        //TODO: generate code
        showAlert("Code Generated", String.format("Generated code for %d entities", entities.size()));
    }

    private void selectEntity(CanvasEntity entity) {
        CanvasState.setSelectedEntity(entity);
        selectedEntityHeader.setText("Selected Entity: " + (entity != null ? entity.getName() : "None"));
        // this doesn't account for someone naming their entity 'None' but come on
    }

    private void updateEntityDisplay(CanvasEntity entity) {
        canvas.getChildren().stream()
                .filter(vBox -> vBox instanceof EntityVBox)
                .map(vBox -> (EntityVBox) vBox)
                .filter(entityNode -> entityNode.getEntity().equals(entity))
                .forEach(EntityVBox::updateDisplay);
    }

    private void redrawCanvas() {
        HashMap<String, CanvasEntity> entities = CanvasState.getEntityMap();

        canvas.getChildren().clear();

        selectEntity(null);

        for(CanvasEntity entity : entities.values()){
            EntityVBox entityVBox = new EntityVBox(entity, canvas);
            entityVBox.setLayoutX(entity.getX());
            entityVBox.setLayoutY(entity.getY());
            entityVBox.setOnMouseClicked(event -> selectEntity(entity));
            canvas.getChildren().add(entityVBox);
        }
    }

    private void handleCanvasClick(MouseEvent event) {
        if (event.getTarget() == canvas) {
            selectEntity(null);
        }
    }

    private Optional<String> getEntityNameFromDialog(){
        TextInputDialog entityNameDialog = new TextInputDialog("");
        entityNameDialog.setHeaderText("Enter Entity Name");
        entityNameDialog.setContentText("Enter the name of your entity to create.");
        return entityNameDialog.showAndWait();
    }

    private Optional<String> getRelationshipSourceEntityFromDialog(){
        HashMap<String, CanvasEntity> entities = CanvasState.getEntityMap();
        List<String> entityChoices = new ArrayList<>(entities.keySet());
        ChoiceDialog<String> relationshipSourceDialog = new ChoiceDialog<>(entityChoices.getFirst(), entityChoices);
        relationshipSourceDialog.setTitle("Select Source Entity");
        relationshipSourceDialog.setHeaderText("Choose the source entity:");

        return relationshipSourceDialog.showAndWait();
    }

    private Optional<String> getRelationshipTargetEntityFromDialog(String sourceEntityName){
        HashMap<String, CanvasEntity> entities = CanvasState.getEntityMap();

        List<String> targetEntityChoices = new ArrayList<>(entities.keySet());
        targetEntityChoices.remove(sourceEntityName);

        ChoiceDialog<String> relationshipTargetDialog = new ChoiceDialog<>(targetEntityChoices.getFirst(), targetEntityChoices);

        relationshipTargetDialog.setTitle("Select Target Entity");
        relationshipTargetDialog.setHeaderText("Choose the target entity:");
        return relationshipTargetDialog.showAndWait();
    }

    private Optional<RelationshipType> getRelationshipTypeFromDialog(){
        ChoiceDialog<RelationshipType> relationshipTypeDialog = new ChoiceDialog<>(
                RelationshipType.ONE_TO_MANY,
                RelationshipType.values()
        );

        relationshipTypeDialog.setTitle("Select Relationship Type");
        relationshipTypeDialog.setHeaderText("Choose the relationship type:");
        return relationshipTypeDialog.showAndWait();
    }

    @FXML
    private void editEntityDialog() throws IOException {
        if (CanvasState.getSelectedEntity() == null) {
            showAlert("No Entity Selected", "Please select an entity first.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(componentsDir + "dialogs/edit-entity-dialog.fxml"));
        DialogPane dialogPane = loader.load();
        EntityController entityController = loader.getController();

        CanvasEntity selectedEntity = CanvasState.getSelectedEntity();
        entityController.loadEntity(selectedEntity);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Edit Entity: " + CanvasState.getSelectedEntity().getName());
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> selectedButton =  dialog.showAndWait();

        if(selectedButton.isPresent() && selectedButton.get().equals(ButtonType.OK)){

            entityController.updateEntity();
            updateEntityDisplay(selectedEntity);

            selectedEntityHeader.setText("Selected Entity: " + selectedEntity.getName());
            showAlert("Entity Updated", "Successfully updated '" + selectedEntity.getName() + "'");
        }
    }

    @FXML
    private void deleteEntityAlert(){
        if (CanvasState.getSelectedEntity() == null) {
            showAlert("No Entity Selected", "Please select an entity first.");
            return;
        }

        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete the entity '" + CanvasState.getSelectedEntity().getName() + "'?",
                confirmButton, cancelButton);
        confirmationDialog.setTitle("Confirm Deletion");
        confirmationDialog.setHeaderText("This action cannot be undone.");

        Optional<ButtonType> selectedButton = confirmationDialog.showAndWait();
        if(selectedButton.isEmpty()) return;

        if(selectedButton.get().equals(confirmButton)){
            deleteEntity();
        }
    }

    @FXML
    private void deleteEntity(){
        if (CanvasState.getSelectedEntity() == null) {
            showAlert("No Entity Selected", "Please select an entity first.");
            return;
        }

        CanvasState.removeEntityByName(CanvasState.getSelectedEntity().getName());
        redrawCanvas();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}