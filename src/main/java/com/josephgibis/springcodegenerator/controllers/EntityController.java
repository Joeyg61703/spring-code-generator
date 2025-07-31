package com.josephgibis.springcodegenerator.controllers;

import com.josephgibis.springcodegenerator.canvas.CanvasEntity;
import com.josephgibis.springcodegenerator.canvas.CanvasManager;
import com.josephgibis.springcodegenerator.canvas.EntityProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EntityController implements Initializable {

    private String componentsDir = "/com/josephgibis/springcodegenerator/components/";
    private final ObservableList<EntityProperty> dialogProperties =  FXCollections.observableArrayList();

    public TextField entityNameField;
    public TextField tableNameField;

    @FXML public TableView<EntityProperty> propertiesTable;
    @FXML public TableColumn<EntityProperty, String> nameColumn;
    @FXML public TableColumn<EntityProperty, String> typeColumn;
    @FXML public TableColumn<EntityProperty, Void> editColumn;
    @FXML public TableColumn<EntityProperty, Void> deleteColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupPropertiesTable();
    }
    private void setupPropertiesTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        editColumn.setCellFactory(param -> new TableCell<EntityProperty, Void>() {
            private final Button editButton = new Button("Edit");
            {
                editButton.setOnAction(e -> {
                    EntityProperty property = getTableView().getItems().get(getIndex());
                    editProperty(property);
                });
                editButton.getStyleClass().add("primary-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editButton);
            }
        });

        deleteColumn.setCellFactory(param -> new TableCell<EntityProperty, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(e -> {
                    EntityProperty property = getTableView().getItems().get(getIndex());
                    dialogProperties.remove(property);
                });
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
    }

    private void editProperty(EntityProperty property) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(componentsDir + "dialogs/add-property-dialog.fxml"));
            DialogPane dialogPane = loader.load();
            PropertyController propertyController = loader.getController();

            propertyController.loadProperty(property);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Property: " + property.getName());
            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> selectedButton =  dialog.showAndWait();

            if(selectedButton.isPresent() && selectedButton.get().equals(ButtonType.OK)){
                EntityProperty updatedProperty = propertyController.createProperty();
                property.setName(updatedProperty.getName());
                property.setType(updatedProperty.getType());
                property.setNullable(updatedProperty.isNullable());
                property.setUnique(updatedProperty.isUnique());

                propertiesTable.refresh();
            }
        } catch (IOException e) {}
    }

    public void loadEntity(CanvasEntity entity){

        entityNameField.setText(entity.getName());
        tableNameField.setText(entity.getTableName());
        //TODO: id type

        for(EntityProperty property : entity.getProperties()){
            EntityProperty propertyCopy = new EntityProperty(
                    property.getName(),
                    property.getType(),
                    property.isNullable(),
                    property.isUnique()
            );
            dialogProperties.add(propertyCopy);
        }

        propertiesTable.setItems(dialogProperties);
        propertiesTable.refresh();

    }

    public void updateEntity(){

        System.out.println("Updating entity: " + CanvasManager.getSelectedEntity().getName());

        CanvasEntity selectedEntity = CanvasManager.getSelectedEntity();
        String originalName = selectedEntity.getName();

        selectedEntity.setName(entityNameField.getText());
        selectedEntity.setTableName(tableNameField.getText());

        selectedEntity.getProperties().clear();
        for(EntityProperty property : dialogProperties){
            EntityProperty propertyCopy = new EntityProperty(
                    property.getName(),
                    property.getType(),
                    property.isNullable(),
                    property.isUnique()
            );
            selectedEntity.getProperties().add(propertyCopy);
        }

        CanvasManager.updateEntityByName(originalName, selectedEntity);
    }



    @FXML
    public void showAddPropertyDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(componentsDir + "dialogs/add-property-dialog.fxml"));
        DialogPane dialogPane = loader.load();
        PropertyController propertyController = loader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Add Property");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> selectedButton =  dialog.showAndWait();

        if(selectedButton.isPresent() && selectedButton.get().equals(ButtonType.OK)){
            EntityProperty newProperty = propertyController.createProperty();
            dialogProperties.add(newProperty);
        }
    }

}
