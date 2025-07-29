package com.josephgibis.springcodegenerator.controllers;

import com.josephgibis.springcodegenerator.model.*;
import com.josephgibis.springcodegenerator.model.EntityProperty;
import com.josephgibis.springcodegenerator.model.enums.PropertyType;
import com.josephgibis.springcodegenerator.model.enums.RelationshipType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private final HashMap<String, CanvasEntity> entities = new HashMap<>();
    private final ObservableList<EntityRelationship> relationships = FXCollections.observableArrayList();

    private CanvasEntity selectedEntity;

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
        entities.put(entityName, newCanvasEntity);

        EntityVBox entityVBox = new EntityVBox(newCanvasEntity, canvas);
        entityVBox.setOnMouseClicked(event -> selectEntity(newCanvasEntity));

        canvas.getChildren().add(entityVBox);

        selectEntity(newCanvasEntity);
    }

    @FXML
    private void addRelationship() {
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
        relationships.add(relationship);

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
        if (entities.isEmpty()) {
            showAlert("No Entities", "No Entities to generate.");
            return;
        }
        //TODO: generate code
        showAlert("Code Generated", String.format("Generated code for %d entities", entities.size()));
    }

    private void selectEntity(CanvasEntity entity) {
        selectedEntity = entity;
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
        List<String> entityChoices = new ArrayList<>(entities.keySet());
        ChoiceDialog<String> relationshipSourceDialog = new ChoiceDialog<>(entityChoices.getFirst(), entityChoices);
        relationshipSourceDialog.setTitle("Select Source Entity");
        relationshipSourceDialog.setHeaderText("Choose the source entity:");

        return relationshipSourceDialog.showAndWait();
    }

    private Optional<String> getRelationshipTargetEntityFromDialog(String sourceEntityName){
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

    private Optional<String> getPropertyTypeFromDialog(){
        String[] propertyTypes = PropertyType.getStringValues();
        ChoiceDialog<String> typeDialog = new ChoiceDialog<>(propertyTypes[0], propertyTypes);
        typeDialog.setTitle("Select Field Type");
        typeDialog.setHeaderText("Choose field type:");

        return typeDialog.showAndWait();
    }

    private Optional<String> getPropertyNameFromDialog(){
        TextInputDialog propertyDialog = new TextInputDialog("propertyName");
        propertyDialog.setTitle("Add Property");
        propertyDialog.setHeaderText("Enter property name:");
        return propertyDialog.showAndWait();
    }

    @FXML
    private void editEntityDialog() throws IOException {
        if (selectedEntity == null) {
            showAlert("No Entity Selected", "Please select an entity first.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/josephgibis/springcodegenerator/components/dialogs/edit-entity-dialog.fxml"));
        DialogPane dialogPane = loader.load();
        EntityController entityController = loader.getController();

        CanvasEntity originalEntity = selectedEntity;
        CanvasEntity entityCopy = new CanvasEntity(originalEntity);

        entityController.loadEntity(entityCopy);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Edit Entity: " + selectedEntity.getName());
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);


        dialog.showAndWait();

    }

    @FXML
    private void deleteEntityAlert(){
        if (selectedEntity == null) {
            showAlert("No Entity Selected", "Please select an entity first.");
            return;
        }

        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete the entity '" + selectedEntity.getName() + "'?",
                confirmButton, cancelButton);
        confirmationDialog.setTitle("Confirm Deletion");
        confirmationDialog.setHeaderText("This action cannot be undone.");

        Optional<ButtonType> response = confirmationDialog.showAndWait();
        if(response.isEmpty()) return;

        if(response.get().equals(confirmButton)){
            deleteEntity();
        }
    }

    @FXML
    private void deleteEntity(){
        if (selectedEntity == null) {
            showAlert("No Entity Selected", "Please select an entity first.");
            return;
        }

        entities.remove(selectedEntity.getName());
        //TODO: remove relation
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