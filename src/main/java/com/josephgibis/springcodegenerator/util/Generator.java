package com.josephgibis.springcodegenerator.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.josephgibis.springcodegenerator.ProjectConfiguration;
import com.josephgibis.springcodegenerator.canvas.CanvasEntity;
import com.josephgibis.springcodegenerator.canvas.CanvasManager;
import com.josephgibis.springcodegenerator.canvas.EntityProperty;
import com.josephgibis.springcodegenerator.canvas.EntityRelationship;
import com.josephgibis.springcodegenerator.canvas.enums.RelationshipType;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

//TODO refactor to file service
public class Generator {

    private final ProjectConfiguration config = ProjectConfiguration.getInstance();
    private final Configuration freeMarkerConfig = new Configuration(Configuration.VERSION_2_3_31);

    private static final Map<String, String> importMap = Map.of(
            "LocalDateTime", "java.time.LocalDateTime",
            "LocalDate", "java.time.LocalDate",
            "UUID", "java.util.UUID",
            "List", "java.util.List");

    private static final Set<String> requiredImports = new HashSet<>();
    private static boolean hasList = false;

    public Generator() {
        freeMarkerConfig.setDefaultEncoding("UTF-8");
    }

    public void generateEntityFile(String entityName) {
        String className = entityName;
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("Entity.java.ftl", entityName);
            String packagePath = config.getBasePackage() + "." + config.getEntityPackage();
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {
        }
    }

    public void generateDTOFiles(String entityName) {
        generateCreateRequestDTOFile(entityName);
        generateUpdateRequestDTOFile(entityName);
        generateResponseDTOFile(entityName);
    }

    private void generateCreateRequestDTOFile(String entityName) {
        String className = "Create" + entityName + "RequestDTO";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("CreateRequestDTO.java.ftl", entityName);
            String packagePath = config.getBasePackage() + "." + config.getDtoPackage();
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {
        }
    }

    private void generateUpdateRequestDTOFile(String entityName) {
        String className = "Update" + entityName + "RequestDTO";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("UpdateRequestDTO.java.ftl", entityName);
            String packagePath = config.getBasePackage() + "." + config.getDtoPackage();
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {
        }
    }

    private void generateResponseDTOFile(String entityName) {
        String className = entityName + "ResponseDTO";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("ResponseDTO.java.ftl", entityName);
            String packagePath = config.getBasePackage() + "." + config.getDtoPackage();
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {
        }
    }

    public void generateServiceFile(String entityName) {
        String className = entityName + "Service";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("Service.java.ftl", entityName);
            String packagePath = config.getBasePackage() + "." + config.getServicePackage();
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {
        }
    }

    public void generateServiceImplFile(String entityName) {
        String className = entityName + "ServiceImpl";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("ServiceImpl.java.ftl", entityName);
            String packagePath = config.getBasePackage() + "." + config.getServicePackage() + ".impl";
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {
        }
    }

    public void generateControllerFile(String entityName) {
        String className = entityName + "Controller";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("Controller.java.ftl", entityName);
            String packagePath = config.getBasePackage() + "." + config.getControllerPackage();
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {
        }
    }

    public void generateRepositoryFile(String entityName) {
        String className = entityName + "Repository";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("Repository.java.ftl", entityName);
            String packagePath = config.getBasePackage() + "." + config.getRepositoryPackage();
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {
        }
    }

    public void generateMapperFile(String entityName) {
        String className = entityName + "Mapper";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("Mapper.java.ftl", entityName);
            String packagePath = config.getBasePackage() + "." + "mappers"; // TODO: replace this
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {
        }
    }

    public void generateNotFoundExceptionFile(String entityName) {
        String className = entityName + "NotFoundException";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("NotFoundException.java.ftl", entityName);
            String packagePath = config.getBasePackage() + "." + "exceptions"; // TODO: replace this
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {
        }
    }

    private String generateFileContentFromTemplate(String templateName, String entityName) {
        try {
            String templatePath = "/com/josephgibis/springcodegenerator/templates/" + templateName;
            InputStream inputStream = Generator.class.getResourceAsStream(templatePath);
            if (inputStream == null) {
                throw new IOException("Template not found in: " + templatePath);
            }

            String templateContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Template template = new Template(templateName, new StringReader(templateContent), freeMarkerConfig);

            Map<String, Object> model = createDataModel(entityName);
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            return writer.toString();

        } catch (IOException | TemplateException e) {
            System.err.println("Error processing template " + templateName + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void writeToFile(String packagePath, String fileName, String fileContent) {
        try {

            String dirPath = packagePath.replace(".", File.separator);
            File targetDir = new File(config.getSourceDirectory(), dirPath);

            // This will generate the packages the user sets if they do not exist (entities,
            // controllers, etc)
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            File targetFile = new File(targetDir, fileName);

            if (targetFile.exists() && !config.isOverwriteFiles()) {
                System.out.println("Failed to create file (" + fileName + "): file already exists");
                return;
            }

            try (FileWriter fileWriter = new FileWriter(targetFile)) {
                fileWriter.write(fileContent);
                System.out.println("Successfully created file: " + fileName);
            }

        } catch (IOException e) {
            System.err.println("Failed to create file (" + fileName + "): " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String timeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    private Map<String, Object> createDataModel(String entityName) {
        Map<String, Object> model = new HashMap<>();

        CanvasEntity canvasEntity = CanvasManager.getCanvasEntityFromName(entityName);

        // Package Info
        model.put("basePackage", config.getBasePackage());
        model.put("entityPackage", config.getEntityPackage());
        model.put("repositoryPackage", config.getRepositoryPackage());
        model.put("servicePackage", config.getServicePackage());
        model.put("controllerPackage", config.getControllerPackage());
        model.put("dtoPackage", config.getDtoPackage());
        model.put("mapperPackage", "mappers"); // TODO: make mappers field
        model.put("exceptionPackage", "exceptions");

        // assuming entityName is in pascalCase we can make other casings
        String pluralEntityName = StringFormatter.makePlural(entityName);

        // Entity Info
        model.put("entityName", entityName);
        model.put("pluralEntityName", pluralEntityName);

        model.put("entityNameCamel", StringFormatter.makeCamelCase(entityName));
        model.put("entityNameSnake", StringFormatter.makeSnakeCase(entityName));
        model.put("entityNamePascal", StringFormatter.makePascalCase(entityName)); // probably is the same but just
                                                                                   // nicer to read in ftl files

        model.put("pluralEntityNameCamel", StringFormatter.makeCamelCase(pluralEntityName));
        model.put("pluralEntityNameSnake", StringFormatter.makeSnakeCase(pluralEntityName)); // (my preferred table
                                                                                             // name)
        model.put("pluralEntityNamePascal", StringFormatter.makePascalCase(pluralEntityName));

        String tableName = "";
        String idType = "Long";

        if (canvasEntity != null) {
            tableName = canvasEntity.getTableName();
            idType = canvasEntity.getIdType();
        }

        if (tableName == null || tableName.trim().isEmpty()) {
            tableName = StringFormatter.makeSnakeCase(pluralEntityName);
        }

        model.put("tableName", tableName);
        model.put("idType", idType);
        model.put("idGeneration", "IDENTITY");

        model.put("properties", getPropertyModelList(canvasEntity));
        model.put("relationships", getRelationshipModelList(canvasEntity));
        model.put("requiredImports", requiredImports);
        model.put("hasList", hasList);

        // Generation Settings
        model.put("useLombok", config.isUseLombok());
        model.put("implementServiceBody", true); // TODO: implement checkbox

        return model;
    }

    private List<Map<String, Object>> getPropertyModelList(CanvasEntity canvasEntity) {
        List<Map<String, Object>> properties = new ArrayList<>();
        for (EntityProperty prop : canvasEntity.getProperties()) {
            Map<String, Object> propMap = new HashMap<>();
            propMap.put("name", prop.getName());
            propMap.put("type", prop.getType());
            propMap.put("nullable", prop.isNullable());
            propMap.put("unique", prop.isUnique());
            propMap.put("includeInCreateRequest", prop.isIncludeInCreateRequest());
            propMap.put("includeInUpdateRequest", prop.isIncludeInUpdateRequest());
            propMap.put("includeInResponse", prop.isIncludeInResponse());

            propMap.put("nameCamel", StringFormatter.makeCamelCase(prop.getName()));
            propMap.put("nameSnake", StringFormatter.makeSnakeCase(prop.getName()));
            propMap.put("namePascal", StringFormatter.makePascalCase(prop.getName()));

            properties.add(propMap);

            String type = prop.getType();
            addImportByType(type);

        }
        return properties;
    }

    private List<Map<String, Object>> getRelationshipModelList(CanvasEntity entity) {
        List<Map<String, Object>> relationships = new ArrayList<>();
        for (EntityRelationship relationship : CanvasManager.getRelationshipsFromSourceEntity(entity)) {
            Map<String, Object> relationshipObject = new HashMap<>();
            relationshipObject.put("sourceEntity", relationship.getSourceEntity());
            relationshipObject.put("targetEntity", relationship.getTargetEntity());
            relationshipObject.put("relationshipType", relationship.getRelationshipType());
            relationshipObject.put("inverseRelationshipType", relationship.getInverseRelationshipType());
            relationshipObject.put("isSource", true);
            relationships.add(relationshipObject);

            String mappedByValue = StringFormatter.makeCamelCase(entity.getName());
            relationshipObject.put("mappedByValue", mappedByValue);

            String foreignKey = relationship.getForeignKeyProperty();
            if (foreignKey != null) {
                relationshipObject.put("foreignKeyProperty", foreignKey);
            }

            if (relationship.getRelationshipType().equals(RelationshipType.ONE_TO_MANY)
                    || relationship.getRelationshipType().equals(RelationshipType.MANY_TO_MANY)) {
                hasList = true;
            }
        }
        for (EntityRelationship relationship : CanvasManager.getRelationshipsFromTargetEntity(entity)) {
            Map<String, Object> relationshipObject = new HashMap<>();
            relationshipObject.put("sourceEntity", relationship.getSourceEntity());
            relationshipObject.put("targetEntity", relationship.getTargetEntity());
            relationshipObject.put("relationshipType", relationship.getRelationshipType());
            relationshipObject.put("inverseRelationshipType", relationship.getInverseRelationshipType());
            relationshipObject.put("isSource", false);
            relationships.add(relationshipObject);

            String mappedByValue = StringFormatter.makeCamelCase(relationship.getSourceEntity().getName());
            relationshipObject.put("mappedByValue", mappedByValue);

            String foreignKey = relationship.getForeignKeyProperty();
            if (foreignKey != null) {
                relationshipObject.put("foreignKeyProperty", foreignKey);
            }

            if (relationship.getRelationshipType().equals(RelationshipType.ONE_TO_MANY)
                    || relationship.getRelationshipType().equals(RelationshipType.MANY_TO_MANY)) {
                hasList = true;
            }
        }
        return relationships;
    }

    private void addImportByType(String type) {
        if (importMap.containsKey(type)) {
            requiredImports.add(importMap.get(type));
        }
    }
}