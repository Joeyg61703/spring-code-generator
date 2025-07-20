package com.josephgibis.springcodegenerator.util;

import com.josephgibis.springcodegenerator.EntityProperty;
import com.josephgibis.springcodegenerator.ProjectConfiguration;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Generator {

    private final ProjectConfiguration config = ProjectConfiguration.getInstance();
    private final Configuration freeMarkerConfig = new Configuration(Configuration.VERSION_2_3_31);

    public Generator() {
        freeMarkerConfig.setDefaultEncoding("UTF-8");
    }

    private String generateFileContentFromTemplate(String templateName) {
        try {
            String templatePath = "/com/josephgibis/springcodegenerator/templates/" + templateName;
            InputStream inputStream = Generator.class.getResourceAsStream(templatePath);
            if (inputStream == null) {
                throw new IOException("Template not found in: " + templatePath);
            }

            String templateContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Template template = new Template(templateName, new StringReader(templateContent), freeMarkerConfig);

            Map<String, Object> model = createDataModel();
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
//            File outputDir = new File("output");
//            if (!outputDir.exists()) {
//                outputDir.mkdirs();
//                System.out.println("Created Output Directory: " + outputDir.getAbsolutePath());
//            }


            String dirPath = packagePath.replace(".", File.separator);
            File targetDir = new File(config.getSourceDirectory(), dirPath);

            // This will generate the packages the user sets if they do not exist (entities, controllers, etc)
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

    public void generateEntityFile() {
        String className = config.getEntityName();
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("Entity.java.ftl");
            String packagePath = config.getBasePackage() + "." + config.getEntityPackage();
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {}
    }

    public void generateDTOFile() {
        String className = config.getEntityName() + "DTO";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("DTO.java.ftl");
            String packagePath = config.getBasePackage() + "." + config.getDtoPackage();
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {}
    }

    public void generateServiceFile() {
        String className = config.getEntityName() + "Service";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("Service.java.ftl");
            String packagePath = config.getBasePackage() + "." + config.getServicePackage();
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {}
    }

    public void generateServiceImplFile() {
        String className = config.getEntityName() + "ServiceImpl";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("ServiceImpl.java.ftl");
            String packagePath = config.getBasePackage() + "." + config.getServicePackage();
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {}
    }

    public void generateControllerFile() {
        String className = config.getEntityName() + "Controller";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("Controller.java.ftl");
            String packagePath = config.getBasePackage() + "." + config.getControllerPackage();
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {}
    }

    public void generateRepositoryFile() {
        String className = config.getEntityName() + "Repository";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);

        try {
            String fileContent = generateFileContentFromTemplate("Repository.java.ftl");
            String packagePath = config.getBasePackage() + "." + config.getRepositoryPackage();
            writeToFile(packagePath, fileName, fileContent);

        } catch (Exception e) {}
    }

    private String timeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    private Map<String, Object> createDataModel() {
        Map<String, Object> model = new HashMap<>();

        // Package Info
        model.put("basePackage", config.getBasePackage());
        model.put("entityPackage", config.getEntityPackage());
        model.put("repositoryPackage", config.getRepositoryPackage());
        model.put("servicePackage", config.getServicePackage());
        model.put("controllerPackage", config.getControllerPackage());
        model.put("dtoPackage", config.getDtoPackage());

        // assuming entityName is in pascalCase we can make other casings
        String entityName = config.getEntityName();
        String pluralEntityName =  StringFormatter.makePlural(entityName);

        // Entity Info
        model.put("entityName", config.getEntityName());
        model.put("pluralEntityName", pluralEntityName);

        model.put("entityNameCamel", StringFormatter.makeCamelCase(entityName));
        model.put("entityNameSnake", StringFormatter.makeSnakeCase(entityName));
        model.put("entityNamePascal", StringFormatter.makePascalCase(entityName)); //probably is the same but just nicer to read in ftl files

        model.put("pluralEntityNameCamel", StringFormatter.makeCamelCase(pluralEntityName));
        model.put("pluralEntityNameSnake", StringFormatter.makeSnakeCase(pluralEntityName)); // (my preferred table name)
        model.put("pluralEntityNamePascal", StringFormatter.makePascalCase(pluralEntityName));

        model.put("extendsClass", config.getExtendsClass());

        String tableName = config.getTableName();
        if (tableName == null || tableName.trim().isEmpty()) {
            tableName = StringFormatter.makeSnakeCase(config.getEntityName());
        }
        model.put("tableName", tableName);

        model.put("idType", config.getIdType());
        model.put("idGeneration", config.getIdGeneration());

        // Entity Properties
        List<Map<String, Object>> templateProperties = new ArrayList<>();
        for (EntityProperty prop : config.getProperties()) {
            Map<String, Object> propMap = new HashMap<>();
            propMap.put("name", prop.getName());
            propMap.put("type", prop.getType());
            propMap.put("nullable", prop.isNullable());
            propMap.put("unique", prop.isUnique());
            propMap.put("defaultValue", prop.getDefaultValue() != null ? prop.getDefaultValue() : "");
            templateProperties.add(propMap);
        }
        model.put("properties", templateProperties);

        // Generation Settings
        model.put("useLombok", config.isUseLombok());

        return model;
    }
}