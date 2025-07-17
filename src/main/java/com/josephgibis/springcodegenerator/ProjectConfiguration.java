package com.josephgibis.springcodegenerator;

import com.josephgibis.springcodegenerator.EntityProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProjectConfiguration {
    // Project Tab Fields
    private final StringProperty sourceDirectory = new SimpleStringProperty("");
    private final StringProperty basePackage = new SimpleStringProperty("com.example.demo");

    // Files to Generate
    private final BooleanProperty generateEntity = new SimpleBooleanProperty(true);
    private final BooleanProperty generateDto = new SimpleBooleanProperty(true);
    private final BooleanProperty generateRepository = new SimpleBooleanProperty(true);
    private final BooleanProperty generateService = new SimpleBooleanProperty(true);
    private final BooleanProperty generateController = new SimpleBooleanProperty(true);

    // Package Names
    private final StringProperty entityPackage = new SimpleStringProperty("entities");
    private final StringProperty dtoPackage = new SimpleStringProperty("dtos");
    private final StringProperty repositoryPackage = new SimpleStringProperty("repositories");
    private final StringProperty servicePackage = new SimpleStringProperty("services");
    private final StringProperty controllerPackage = new SimpleStringProperty("controllers");

    // Entity Tab Fields
    private final StringProperty entityName = new SimpleStringProperty("");
    private final StringProperty tableName = new SimpleStringProperty("");
    private final StringProperty idType = new SimpleStringProperty("Long");
    private final StringProperty idGeneration = new SimpleStringProperty("IDENTITY");
    private final StringProperty extendsClass = new SimpleStringProperty("");

    // Entity Properties
    private final ObservableList<EntityProperty> properties = FXCollections.observableArrayList();

    // Generation Settings
    private final BooleanProperty overwriteFiles = new SimpleBooleanProperty(false);
    private final BooleanProperty useLombok = new SimpleBooleanProperty(true);
    private final BooleanProperty addValidation = new SimpleBooleanProperty(false);
    private final BooleanProperty generateTests = new SimpleBooleanProperty(false);

    // Singleton
    private static ProjectConfiguration instance;

    private ProjectConfiguration() {}

    public static ProjectConfiguration getInstance() {
        if (instance == null) {
            instance = new ProjectConfiguration();
        }
        return instance;
    }

    // Getters and Property methods
    public StringProperty sourceDirectoryProperty() { return sourceDirectory; }
    public String getSourceDirectory() { return sourceDirectory.get(); }
    public void setSourceDirectory(String sourceDirectory) { this.sourceDirectory.set(sourceDirectory); }

    public StringProperty basePackageProperty() { return basePackage; }
    public String getBasePackage() { return basePackage.get(); }
    public void setBasePackage(String basePackage) { this.basePackage.set(basePackage); }

    public BooleanProperty generateEntityProperty() { return generateEntity; }
    public boolean isGenerateEntity() { return generateEntity.get(); }
    public void setGenerateEntity(boolean generateEntity) { this.generateEntity.set(generateEntity); }

    public BooleanProperty generateDtoProperty() { return generateDto; }
    public boolean isGenerateDto() { return generateDto.get(); }
    public void setGenerateDto(boolean generateDto) { this.generateDto.set(generateDto); }

    public BooleanProperty generateRepositoryProperty() { return generateRepository; }
    public boolean isGenerateRepository() { return generateRepository.get(); }
    public void setGenerateRepository(boolean generateRepository) { this.generateRepository.set(generateRepository); }

    public BooleanProperty generateServiceProperty() { return generateService; }
    public boolean isGenerateService() { return generateService.get(); }
    public void setGenerateService(boolean generateService) { this.generateService.set(generateService); }

    public BooleanProperty generateControllerProperty() { return generateController; }
    public boolean isGenerateController() { return generateController.get(); }
    public void setGenerateController(boolean generateController) { this.generateController.set(generateController); }

    public StringProperty entityPackageProperty() { return entityPackage; }
    public String getEntityPackage() { return entityPackage.get(); }
    public void setEntityPackage(String entityPackage) { this.entityPackage.set(entityPackage); }

    public StringProperty dtoPackageProperty() { return dtoPackage; }
    public String getDtoPackage() { return dtoPackage.get(); }
    public void setDtoPackage(String dtoPackage) { this.dtoPackage.set(dtoPackage); }

    public StringProperty repositoryPackageProperty() { return repositoryPackage; }
    public String getRepositoryPackage() { return repositoryPackage.get(); }
    public void setRepositoryPackage(String repositoryPackage) { this.repositoryPackage.set(repositoryPackage); }

    public StringProperty servicePackageProperty() { return servicePackage; }
    public String getServicePackage() { return servicePackage.get(); }
    public void setServicePackage(String servicePackage) { this.servicePackage.set(servicePackage); }

    public StringProperty controllerPackageProperty() { return controllerPackage; }
    public String getControllerPackage() { return controllerPackage.get(); }
    public void setControllerPackage(String controllerPackage) { this.controllerPackage.set(controllerPackage); }

    public StringProperty entityNameProperty() { return entityName; }
    public String getEntityName() { return entityName.get(); }
    public void setEntityName(String entityName) { this.entityName.set(entityName); }

    public StringProperty tableNameProperty() { return tableName; }
    public String getTableName() { return tableName.get(); }
    public void setTableName(String tableName) { this.tableName.set(tableName); }

    public StringProperty idTypeProperty() { return idType; }
    public String getIdType() { return idType.get(); }
    public void setIdType(String idType) { this.idType.set(idType); }

    public StringProperty idGenerationProperty() { return idGeneration; }
    public String getIdGeneration() { return idGeneration.get(); }
    public void setIdGeneration(String idGeneration) { this.idGeneration.set(idGeneration); }

    public StringProperty extendsClassProperty() { return extendsClass; }
    public String getExtendsClass() { return extendsClass.get(); }
    public void setExtendsClass(String extendsClass) { this.extendsClass.set(extendsClass); }

    public ObservableList<EntityProperty> getProperties() { return properties; }

    public BooleanProperty overwriteFilesProperty() { return overwriteFiles; }
    public boolean isOverwriteFiles() { return overwriteFiles.get(); }
    public void setOverwriteFiles(boolean overwriteFiles) { this.overwriteFiles.set(overwriteFiles); }

    public BooleanProperty useLombokProperty() { return useLombok; }
    public boolean isUseLombok() { return useLombok.get(); }
    public void setUseLombok(boolean useLombok) { this.useLombok.set(useLombok); }

    public BooleanProperty addValidationProperty() { return addValidation; }
    public boolean isAddValidation() { return addValidation.get(); }
    public void setAddValidation(boolean addValidation) { this.addValidation.set(addValidation); }

    public BooleanProperty generateTestsProperty() { return generateTests; }
    public boolean isGenerateTests() { return generateTests.get(); }
    public void setGenerateTests(boolean generateTests) { this.generateTests.set(generateTests); }

}