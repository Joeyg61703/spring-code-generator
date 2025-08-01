package com.josephgibis.springcodegenerator.controllers;

import com.josephgibis.springcodegenerator.canvas.CanvasManager;
import com.josephgibis.springcodegenerator.canvas.EntityRelationship;
import com.josephgibis.springcodegenerator.canvas.enums.RelationshipType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RelationshipController implements Initializable {

    @FXML private TableView<EntityRelationship> relationshipsTable;
    @FXML private TableColumn<EntityRelationship, String> sourceColumn;
    @FXML private TableColumn<EntityRelationship, String> targetColumn;
    @FXML private TableColumn<EntityRelationship, RelationshipType> typeColumn;
    @FXML private TableColumn<EntityRelationship, Void> editColumn;
    @FXML private TableColumn<EntityRelationship, Void> deleteColumn;

    private final ObservableList<EntityRelationship> relationshipData = FXCollections.observableArrayList(); //(copy)

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupRelationshipsTable();
        loadRelationships();
    }

    private void setupRelationshipsTable() {
        sourceColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().getSourceEntity().getName(),
                        cellData.getValue().getSourceEntity().nameProperty()
                )
        );

        targetColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().getTargetEntity().getName(),
                        cellData.getValue().getTargetEntity().nameProperty()
                )
        );

        typeColumn.setCellValueFactory(new PropertyValueFactory<>("relationshipType"));

        editColumn.setCellFactory(param -> new TableCell<EntityRelationship, Void>() {
            private final Button editButton = new Button("Edit");
            {
                editButton.setOnAction(e -> {
                    EntityRelationship relationship = getTableView().getItems().get(getIndex());
                    editRelationship(relationship);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editButton);
            }
        });

        deleteColumn.setCellFactory(param -> new TableCell<EntityRelationship, Void>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(e -> {
                    EntityRelationship relationship = getTableView().getItems().get(getIndex());
                    deleteRelationship(relationship);
                });
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        relationshipsTable.setItems(relationshipData);
        relationshipsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadRelationships() {
        relationshipData.clear();
        relationshipData.addAll(CanvasManager.getRelationships());
    }

    public List<EntityRelationship> getUpdatedRelationships(){
        return this.relationshipData;
    }

    private void editRelationship(EntityRelationship relationship) {
        List<String> entityNames = CanvasManager.getEntityNameList();
        if (entityNames.size() < 2) {
            showAlert("Cannot Edit", "Need at least 2 entities to edit relationships.");
            return;
        }

        Optional<String> sourceDialogInput = getRelationshipSourceEntityFromDialog();
        if(sourceDialogInput.isEmpty()) return;
        String newSourceName = sourceDialogInput.get();

        Optional<String> targetDialogInput = getRelationshipTargetEntityFromDialog(newSourceName);
        if(targetDialogInput.isEmpty()) return;
        String newTargetName = targetDialogInput.get();

        Optional<RelationshipType> relationTypeInput = getRelationshipTypeFromDialog();
        if(relationTypeInput.isEmpty()) return;
        RelationshipType newType = relationTypeInput.get();

        relationship.setSourceEntity(CanvasManager.getCanvasEntityFromName(newSourceName));
        relationship.setTargetEntity(CanvasManager.getCanvasEntityFromName(newTargetName));
        relationship.setRelationshipType(newType);
        relationshipsTable.refresh();

        showAlert("Success", "Relationship updated successfully!");
    }

    private void deleteRelationship(EntityRelationship relationship) {
        Optional<ButtonType> selectedButton = deleteRelationshipAlert(relationship);
        if (selectedButton.isPresent() && selectedButton.get().equals(ButtonType.OK)) {
            CanvasManager.removeRelationship(relationship);
            relationshipData.remove(relationship);
            showAlert("Success", "Relationship deleted successfully!");
        }
    }


    // ===================================
    //               POPUPS
    // ===================================

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

    private Optional<ButtonType> deleteRelationshipAlert(EntityRelationship relationship){
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Relationship");
        confirmDialog.setHeaderText("Are you sure you want to delete this relationship?");
        confirmDialog.setContentText(String.format("From %s to %s (%s)",
                relationship.getSourceEntity().getName(),
                relationship.getTargetEntity().getName(),
                relationship.getRelationshipType()));

        return confirmDialog.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}