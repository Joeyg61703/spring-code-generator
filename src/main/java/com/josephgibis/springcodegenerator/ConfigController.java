package com.josephgibis.springcodegenerator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class ConfigController  {
    @FXML private Button browseFilesButton;

    // Basic info
    @FXML private TextField entityNameField;
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

    // Action button
    @FXML private Button generateButton;

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
}
