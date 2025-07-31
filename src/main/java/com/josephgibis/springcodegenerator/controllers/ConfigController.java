package com.josephgibis.springcodegenerator.controllers;

import com.josephgibis.springcodegenerator.ProjectConfiguration;
import com.josephgibis.springcodegenerator.util.Generator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ConfigController implements Initializable {

    private final ProjectConfiguration config = ProjectConfiguration.getInstance();

    // Basic info
    @FXML private TextField basePackageField;
    @FXML private TextField sourceDirectoryField;

    // Files to generate
    @FXML private CheckBox entityCheckBox;
    @FXML private CheckBox dtoCheckBox;
    @FXML private CheckBox repositoryCheckBox;
    @FXML private CheckBox serviceCheckBox;
    @FXML private CheckBox controllerCheckBox;

    // Folder names
    @FXML private TextField entityPackageField;
    @FXML private TextField dtoPackageField;
    @FXML private TextField repositoryPackageField;
    @FXML private TextField servicePackageField;
    @FXML private TextField controllerPackageField;

    // Generation settings
    @FXML private CheckBox overwriteFilesCheckBox;
    @FXML private CheckBox useLombokCheckBox;
    @FXML private CheckBox addValidationCheckBox;
    @FXML private CheckBox generateTestsCheckBox;


    @FXML
    private void browseSourceDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Source Directory");

        String currentPath = sourceDirectoryField.getText();
        if (!currentPath.isEmpty()) {
            File currentDir = new File(currentPath);
            if (currentDir.exists() && currentDir.isDirectory()) {
                directoryChooser.setInitialDirectory(currentDir);
            }
        }

        File selectedDirectory = directoryChooser.showDialog(sourceDirectoryField.getScene().getWindow());
        if (selectedDirectory != null) {
            sourceDirectoryField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void generateFiles() {
        if (!config.isValid()) {
            showAlert("Validation Error", config.getValidationError());
            return;
        }

        Generator generator = new Generator();

        if (config.isGenerateEntity()) {
            generator.generateEntityFile();
        }
        if (config.isGenerateDto()) {
            generator.generateDTOFile();
        }
        if (config.isGenerateRepository()) {
            generator.generateRepositoryFile();
        }
        if (config.isGenerateService()) {
            generator.generateServiceFile();
        }
        if (config.isGenerateController()) {
            generator.generateControllerFile();
        }

        showAlert("Success", "Files generated successfully!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupBindings();
    }

    private void setupBindings() {
        // Project Tab Bindings
        if (sourceDirectoryField != null) {
            sourceDirectoryField.textProperty().bindBidirectional(config.sourceDirectoryProperty());
        }
        if (basePackageField != null) {
            basePackageField.textProperty().bindBidirectional(config.basePackageProperty());
        }
        if (entityCheckBox != null) {
            entityCheckBox.selectedProperty().bindBidirectional(config.generateEntityProperty());
        }
        if (dtoCheckBox != null) {
            dtoCheckBox.selectedProperty().bindBidirectional(config.generateDtoProperty());
        }
        if (repositoryCheckBox != null) {
            repositoryCheckBox.selectedProperty().bindBidirectional(config.generateRepositoryProperty());
        }
        if (serviceCheckBox != null) {
            serviceCheckBox.selectedProperty().bindBidirectional(config.generateServiceProperty());
        }
        if (controllerCheckBox != null) {
            controllerCheckBox.selectedProperty().bindBidirectional(config.generateControllerProperty());
        }
        if (entityPackageField != null) {
            entityPackageField.textProperty().bindBidirectional(config.entityPackageProperty());
        }
        if (dtoPackageField != null) {
            dtoPackageField.textProperty().bindBidirectional(config.dtoPackageProperty());
        }
        if (repositoryPackageField != null) {
            repositoryPackageField.textProperty().bindBidirectional(config.repositoryPackageProperty());
        }
        if (servicePackageField != null) {
            servicePackageField.textProperty().bindBidirectional(config.servicePackageProperty());
        }
        if (controllerPackageField != null) {
            controllerPackageField.textProperty().bindBidirectional(config.controllerPackageProperty());
        }

        // Generate Tab Bindings
        if (overwriteFilesCheckBox != null) {
            overwriteFilesCheckBox.selectedProperty().bindBidirectional(config.overwriteFilesProperty());
        }
        if (useLombokCheckBox != null) {
            useLombokCheckBox.selectedProperty().bindBidirectional(config.useLombokProperty());
        }
        if (addValidationCheckBox != null) {
            addValidationCheckBox.selectedProperty().bindBidirectional(config.addValidationProperty());
        }
        if (generateTestsCheckBox != null) {
            generateTestsCheckBox.selectedProperty().bindBidirectional(config.generateTestsProperty());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
