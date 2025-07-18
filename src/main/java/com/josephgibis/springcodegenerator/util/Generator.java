package com.josephgibis.springcodegenerator.util;

import com.josephgibis.springcodegenerator.EntityProperty;
import com.josephgibis.springcodegenerator.ProjectConfiguration;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Generator {

    // Get access to project configuration for entity properties
    private final ProjectConfiguration config = ProjectConfiguration.getInstance();

    public Generator() {
    }

    public void generateEntityFile(){

        String className = config.getEntityName();
        String fileName = className + ".java";
        String filePath = config.getSourceDirectory() + File.separator + fileName;

        System.out.println(timeStamp() + ": Generating " + fileName);

        if(config.isOverwriteFiles() && new File(filePath).isFile()){

        }

        StringBuilder sb = new StringBuilder();

        sb.append("package " + config.getBasePackage() + "." + config.getEntityPackage() + ";\n");
        appendImports(sb);
        appendEntityAnnotations(sb);
        sb.append("public class " + className + "{\n");

        appendIdProperty(sb);
        for(EntityProperty property : config.getProperties()){
            appendProperty(sb, property.getName(), property.getType(), property.getDefaultValue());
        }

        for(EntityProperty property : config.getProperties()){
            appendGetterAndSetter(sb, property.getName(), property.getType());
        }

        sb.append("}\n");

        System.out.println(sb.toString());
    }

    public void generateDTOFile(){
        StringBuilder sb = new StringBuilder();
        String className = config.getEntityName() + "DTO";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);
    }

    public void generateServiceFile(){
        StringBuilder sb = new StringBuilder();
        String className = config.getEntityName() + "Service";
        String fileName = className + ".java";
        System.out.println(timeStamp() + ": Generating " + fileName);

    }

    public void generateServiceImplFile(){
        StringBuilder sb = new StringBuilder();
        String className = config.getEntityName() + "ServiceImpl";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);
    }
    public void generateControllerFile(String packageName){
        StringBuilder sb = new StringBuilder();
        String className = config.getEntityName() + "Controller";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);
    }
    public void generateRepositoryFile(){
        StringBuilder sb = new StringBuilder();
        String className = config.getEntityName() + "Repository";
        String fileName = className + ".java";

        System.out.println(timeStamp() + ": Generating " + fileName);
    }


    private String timeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }


    private String generateConstructor(){
        StringBuilder sb = new StringBuilder();


        return sb.toString();
    }

    private String addTab(String line){
        //TODO: add a tab preference
        return "\t" + line;
    }

    private List<String> indent(List<String> lines){
        List<String> list = new ArrayList<String>();
        for(String line : lines){
            list.add(addTab(line));
        }
        return list;
    }

    private void appendImports(StringBuilder sb){

    }

    private void appendEntityAnnotations(StringBuilder sb){

        sb.append("@Entity\n");

        String tableName = StringFormatter.makeSnakeCase(config.getEntityName());
        if(!config.getTableName().trim().isEmpty()) {
            tableName = config.getTableName();
        }

        sb.append("@Table(name = \"" + tableName  + "\")\n");


        if(config.isUseLombok()){
            sb.append("@Getter\n");
            sb.append("@Setter\n");
            sb.append("@AllArgsConstructor\n");
            sb.append("@NoArgsConstructor\n");
        }
    }



    private void appendNoArgsConstructor(StringBuilder sb, String className){
        if(!config.isUseLombok()){
            sb.append("public " + className + "(){\n");
        }
    }

    private void appendAllArgsConstructor(StringBuilder sb, String className){
        if(!config.isUseLombok()){
            //TODO: generate constructor params
            sb.append("public " + className + "(){\n");
        }
    }

    private void appendIdProperty(StringBuilder sb){
        if(!config.isUseLombok()){
            sb.append("@Id\n");
            sb.append("@GeneratedValue(strategy = GenerationType." + config.getIdGeneration() +")\n");
            sb.append("private " + config.getIdType() + " id;\n");
        }
    }

    private void appendProperty(StringBuilder sb, String propertyName, String type, String defaultValue){

        if(type.equals("String")){
            defaultValue = "\"" + defaultValue + "\"";
        }
        sb.append("private " + type + " " + propertyName + " = " + defaultValue +";\n");
    }

    private void appendGetterAndSetter(StringBuilder sb, String propertyName, String type){

        if(!config.isUseLombok()){
            sb.append("public " + type + " get" + StringFormatter.makePascalCase(propertyName) + "(){\n");
            sb.append("\treturn " + propertyName + ";\n");
            sb.append("}\n");
            sb.append("\n");

            sb.append("public void set" + StringFormatter.makePascalCase(propertyName) + "(" + type + " " + propertyName + "){\n");
            sb.append("\tthis." + propertyName + " = " + propertyName + ";\n");
            sb.append("}\n");
            sb.append("\n");
        }

    }


}
