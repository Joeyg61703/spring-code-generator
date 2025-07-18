package com.josephgibis.springcodegenerator;

import com.josephgibis.springcodegenerator.util.Generator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ConfigController implements Initializable {

    private final ProjectConfiguration config = ProjectConfiguration.getInstance();


    @FXML private Button addPropertyButton;

    @FXML private Button browseFilesButton;

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

    @FXML private Button generateButton;

    // Entity Details
    @FXML private TextField entityNameField;
    @FXML private TextField tableNameField;
    @FXML private ComboBox<String> idTypeCombo;
    @FXML private ComboBox<String> idGenerationCombo;
    @FXML private TextField extendsField;

    // Entity Properties
    @FXML private TableView<EntityProperty> propertiesTable;
    @FXML private TableColumn<EntityProperty, String> propertyNameColumn;
    @FXML private TableColumn<EntityProperty, String> propertyTypeColumn;
    @FXML private TableColumn<EntityProperty, Boolean> propertyNullableColumn;
    @FXML private TableColumn<EntityProperty, Boolean> propertyUniqueColumn;
    @FXML private TableColumn<EntityProperty, String> propertyDefaultColumn;
    @FXML private TableColumn<EntityProperty, Void> actionsColumn;


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
        // Validate configuration
        if (!config.isValid()) {
            showAlert("Validation Error", config.getValidationError());
            return;
        }

        Generator generator = new Generator(
                config.getEntityName(),
                config.getBasePackage(),
                config.isUseLombok(),
                config.isOverwriteFiles()
        );

        if (config.isGenerateEntity()) {
            generator.generateEntityFile(config.getEntityPackage());
        }
        if (config.isGenerateDto()) {
            generator.generateDTOFile(config.getDtoPackage());
        }
        if (config.isGenerateRepository()) {
            generator.generateRepositoryFile(config.getRepositoryPackage());
        }
        if (config.isGenerateService()) {
            generator.generateServiceFile(config.getServicePackage());
        }
        if (config.isGenerateController()) {
            generator.generateControllerFile(config.getControllerPackage());
        }

        showAlert("Success", "Files generated successfully!");
    }



    public void addProperty(ActionEvent event) {
        config.getProperties().add(new EntityProperty());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupBindings();

        if(propertiesTable != null) {
            propertiesTable.setItems(config.getProperties());
            propertiesTable.setEditable(true);

            propertyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            propertyNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            propertyNameColumn.setOnEditCommit(e -> e.getRowValue().setName(e.getNewValue()));

            propertyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            propertyTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                    "String", "Integer", "Long", "Double", "Boolean", "LocalDate", "LocalDateTime", "BigDecimal"
            ));
            propertyTypeColumn.setOnEditCommit(e -> e.getRowValue().setType(e.getNewValue()));

            propertyNullableColumn.setCellValueFactory(new PropertyValueFactory<>("nullable"));
            propertyNullableColumn.setCellFactory(CheckBoxTableCell.forTableColumn(propertyNullableColumn));

            propertyUniqueColumn.setCellValueFactory(new PropertyValueFactory<>("unique"));
            propertyUniqueColumn.setCellFactory(CheckBoxTableCell.forTableColumn(propertyUniqueColumn));

            propertyDefaultColumn.setCellValueFactory(new PropertyValueFactory<>("defaultValue"));
            propertyDefaultColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            propertyDefaultColumn.setOnEditCommit(e -> e.getRowValue().setDefaultValue(e.getNewValue()));

            actionsColumn.setCellFactory(param -> new TableCell<EntityProperty, Void>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(e -> {
                        EntityProperty property = getTableView().getItems().get(getIndex());
                        config.getProperties().remove(property);
                    });
                    deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 16px;");
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            });
        }
        setupIdGenerationCombo();
        setupIdTypeCombo();
    }
    private void setupIdTypeCombo() {
        if (idTypeCombo != null) {
            idTypeCombo.setItems(FXCollections.observableArrayList(
                    "Long",
                    "UUID",
                    "Integer",
                    "String",
                    "BigInteger"

            ));
            idTypeCombo.getSelectionModel().select("Long");
        }
    }

    private void setupIdGenerationCombo() {
        if (idGenerationCombo != null) {
            idGenerationCombo.setItems(FXCollections.observableArrayList(
                    "IDENTITY",
                    "UUID",
                    "SEQUENCE",
                    "TABLE",
                    "AUTO",
                    "ASSIGNED",
                    "NONE"
            ));
            idGenerationCombo.getSelectionModel().select("IDENTITY");
        }
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

        // Entity Tab Bindings
        if (entityNameField != null) {
            entityNameField.textProperty().bindBidirectional(config.entityNameProperty());
        }
        if (tableNameField != null) {
            tableNameField.textProperty().bindBidirectional(config.tableNameProperty());
        }
        if (idTypeCombo != null) {
            idTypeCombo.valueProperty().bindBidirectional(config.idTypeProperty());
        }
        if (idGenerationCombo != null) {
            idGenerationCombo.valueProperty().bindBidirectional(config.idGenerationProperty());
        }
        if (extendsField != null) {
            extendsField.textProperty().bindBidirectional(config.extendsClassProperty());
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
