package com.josephgibis.springcodegenerator.controllers;

import com.josephgibis.springcodegenerator.SaveSystem;
import com.josephgibis.springcodegenerator.canvas.*;
import com.josephgibis.springcodegenerator.canvas.enums.RelationshipType;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CanvasController implements Initializable {

    @FXML public Text selectedEntityHeader;
    @FXML public Text selectedRelationshipHeader;
    @FXML private Pane canvas;

    private String componentsDir = "/com/josephgibis/springcodegenerator/components/";
    private final SaveSystem saveSystem = new SaveSystem();


    //TODO: prevent duplicate names

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CanvasManager.setCanvas(canvas);
        CanvasManager.setSelectedEntityHeader(selectedEntityHeader);
        CanvasManager.setSelectedRelationshipHeader(selectedRelationshipHeader);
        canvas.setOnMousePressed(this::handleCanvasPress);
    }

    // ==============================
    //            ENTITIES
    // ==============================
    @FXML
    private void addNewEntity() {
        Optional<String> entityNameInput = getEntityNameFromDialog();
        if(entityNameInput.isEmpty()) return;

        String entityName = entityNameInput.get().trim();
        if(entityName.isEmpty()){
            showAlert("Failed to Create Entity", "Cannot create entity without a name.");
            return;
        }

        CanvasManager.addEntity(new CanvasEntity(entityName));
    }

    @FXML
    private void deleteSelectedEntity(){
        if (CanvasManager.getSelectedEntity() == null) {
            showAlert("No Entity Selected", "Please select an entity first.");
            return;
        }
        CanvasManager.removeEntity(CanvasManager.getSelectedEntity().getName());
    }
    // ==============================
    //          RELATIONSHIPS
    // ==============================
    @FXML
    private void addRelationship() {
        if (CanvasManager.getEntityVBoxMap().size() < 2) {
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

        CanvasManager.createRelationshipByNames(sourceEntityName, targetEntityName, relationType);

        //TODO: create table for many to many?
    }

    @FXML
    private void deleteSelectedRelationship(){
        if (CanvasManager.getSelectedRelationship() == null) {
            showAlert("No Relationship Selected", "Please select a relationship first.");
            return;
        }
        CanvasManager.removeRelationship(CanvasManager.getSelectedRelationship());
    }

    private void handleCanvasPress(MouseEvent event) {
        if (event.getTarget() == canvas) {
            CanvasManager.setSelectedEntity(null);
            CanvasManager.setSelectedRelationship(null);
        }
    }

    // ==============================
    //            POPUPS
    // ==============================

    private Optional<String> getEntityNameFromDialog(){
        TextInputDialog entityNameDialog = new TextInputDialog("");
        entityNameDialog.setHeaderText("Enter Entity Name");
        entityNameDialog.setContentText("Enter the name of your entity to create.");
        return entityNameDialog.showAndWait();
    }

    private Optional<String> getRelationshipSourceEntityFromDialog(){
        List<String> entityChoices = CanvasManager.getEntityNameList();
        ChoiceDialog<String> relationshipSourceDialog = new ChoiceDialog<>(entityChoices.getFirst(), entityChoices);
        relationshipSourceDialog.setTitle("Select Source Entity");
        relationshipSourceDialog.setHeaderText("Choose the source entity:");

        return relationshipSourceDialog.showAndWait();
    }

    private Optional<String> getRelationshipTargetEntityFromDialog(String sourceEntityName){
        List<String> targetEntityChoices = CanvasManager.getEntityNameList();
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
        if (CanvasManager.getSelectedEntity() == null) {
            showAlert("No Entity Selected", "Please select an entity first.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(componentsDir + "dialogs/edit-entity-dialog.fxml"));
        DialogPane dialogPane = loader.load();
        EntityController entityController = loader.getController();

        CanvasEntity selectedEntity = CanvasManager.getSelectedEntity();
        entityController.loadEntity(selectedEntity);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Edit Entity: " + CanvasManager.getSelectedEntity().getName());
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> selectedButton =  dialog.showAndWait();
        if(selectedButton.isPresent() && selectedButton.get().equals(ButtonType.OK)){
            entityController.updateEntity();
            selectedEntityHeader.setText("Selected Entity: " + selectedEntity.getName());
            showAlert("Entity Updated", "Successfully updated '" + selectedEntity.getName() + "'");
        }
    }

    @FXML
    private void deleteEntityAlert(){
        if (CanvasManager.getSelectedEntity() == null) {
            showAlert("No Entity Selected", "Please select an entity first.");
            return;
        }

        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete the entity '" + CanvasManager.getSelectedEntity().getName() + "'?",
                confirmButton, cancelButton);
        confirmationDialog.setTitle("Confirm Deletion");
        confirmationDialog.setHeaderText("This action cannot be undone.");

        Optional<ButtonType> selectedButton = confirmationDialog.showAndWait();

        if(selectedButton.isPresent() && selectedButton.get().equals(confirmButton)){
            deleteSelectedEntity();
        }
    }

    @FXML
    private void deleteRelationshipAlert(){
        if (CanvasManager.getSelectedRelationship() == null) {
            showAlert("No Relationship Selected", "Please select a relationship first.");
            return;
        }

        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete the relationship '" + CanvasManager.getSelectedRelationship().compositeKey() + "'?",
                confirmButton, cancelButton);
        confirmationDialog.setTitle("Confirm Deletion");
        confirmationDialog.setHeaderText("This action cannot be undone.");

        Optional<ButtonType> selectedButton = confirmationDialog.showAndWait();

        if(selectedButton.isPresent() && selectedButton.get().equals(confirmButton)){
            deleteSelectedRelationship();
        }
    }

    @FXML
    private void editRelationshipDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(componentsDir + "dialogs/edit-relationship-dialog.fxml"));
        DialogPane dialogPane = loader.load();
        RelationshipController relationshipController = loader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Manage Relationships");
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);
        dialogPane.getButtonTypes().add(ButtonType.OK);

        Optional<ButtonType> selectedButton = dialog.showAndWait();

        if(selectedButton.isPresent() && selectedButton.get().equals(ButtonType.OK)) {
            List<EntityRelationship> updatedRelationships = relationshipController.getUpdatedRelationships();
            CanvasManager.updateRelationships(updatedRelationships);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ========================================
    //                 SAVE/LOAD
    // ========================================

    @FXML
    private void saveProject() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Canvas Project");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        fileChooser.setInitialFileName("canvas-project.json");

        File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());
        if (file != null) {
            try {
                saveSystem.saveCanvasToFile(file);
                showAlert("Success", "Project saved successfully to " + file.getName());
            } catch (IOException e) {
                showAlert("Error", "Failed to save project: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void loadProject() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Canvas Project");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file != null) {
            try {
                saveSystem.loadFileToCanvas(file);
                showAlert("Success", "Project loaded successfully from " + file.getName());
            } catch (IOException e) {
                showAlert("Error", "Failed to load project: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void clearProject() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Clear Project");
        confirmDialog.setHeaderText("Clear Canvas");
        confirmDialog.setContentText("Are you sure you want to clear your project? This will delete everything");

        Optional<ButtonType> selectedButton = confirmDialog.showAndWait();
        if (selectedButton.isPresent() && selectedButton.get() == ButtonType.OK) {
            CanvasManager.clearCanvas();
            showAlert("Success", "Canvas Cleared");
        }
    }

}