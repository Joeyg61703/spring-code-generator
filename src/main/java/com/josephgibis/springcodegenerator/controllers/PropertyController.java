package com.josephgibis.springcodegenerator.controllers;

import com.josephgibis.springcodegenerator.canvas.EntityProperty;
import com.josephgibis.springcodegenerator.canvas.enums.PropertyType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PropertyController implements Initializable {

    @FXML private TextField propertyNameField;
    @FXML private ComboBox<String> propertyTypeCombo;
    @FXML private TextField defaultValueField;
    @FXML private CheckBox nullableCheckBox;
    @FXML private CheckBox uniqueCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupPropertyTypeCombo();
    }

    private void setupPropertyTypeCombo() {
        propertyTypeCombo.setItems(FXCollections.observableArrayList(
                PropertyType.getStringValues()
        ));
        propertyTypeCombo.getSelectionModel().select("String");
    }

    public void loadProperty(EntityProperty property) {
        if (property != null) {
            propertyNameField.setText(property.getName());
            propertyTypeCombo.setValue(property.getType());
            nullableCheckBox.setSelected(property.isNullable());
            uniqueCheckBox.setSelected(property.isUnique());
            //TODO: add default value
        }
    }

    public EntityProperty createProperty() {
        String name = propertyNameField.getText().trim();
        String type = propertyTypeCombo.getValue();
        boolean nullable = nullableCheckBox.isSelected();
        boolean unique = uniqueCheckBox.isSelected();
        //TODO: add default value
        return new EntityProperty(name, type, nullable, unique);
    }

}