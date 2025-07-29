package com.josephgibis.springcodegenerator.controllers;

import com.josephgibis.springcodegenerator.model.CanvasEntity;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class EntityController {

    public TextField entityNameField;
    public TextField tableNameField;

    public TableView propertiesTable;
    public TableColumn nameColumn;
    public TableColumn typeColumn;
    public TableColumn editColumn;
    public TableColumn deleteColumn;


    public void loadEntity(CanvasEntity entity){

        entityNameField.setText(entity.getName());
        tableNameField.setText(entity.getTableName());
        //TODO: id type
    }

    public void updateEntity(){

        CanvasEntity entity = new CanvasEntity(entityNameField.getText());
        entity.setTableName(tableNameField.getText());

    }

    public void addProperty(ActionEvent actionEvent) {
    }
}
