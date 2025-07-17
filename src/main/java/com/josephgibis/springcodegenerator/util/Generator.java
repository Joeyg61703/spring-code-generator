package com.josephgibis.springcodegenerator.util;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Generator {

    private String entityName = "";
    private File sourceDir;
    private String packageName = "";
    private boolean useLombok = true;
    private boolean overwriteFiles = false;

    public Generator(String entityName, String packageName, boolean useLombok, boolean overwriteFiles) {
        this.packageName = packageName;
        this.entityName = entityName;
        this.useLombok = useLombok;
        this.overwriteFiles = overwriteFiles;
    }



    public void generateEntityFile(String packageName){
        String fileName = entityName + ".java";
        System.out.println(timeStamp() + ": Generating " + fileName);
    }

    public void generateDTOFile(String packageName){
        String fileName = entityName + "DTO.java";
        System.out.println(timeStamp() + ": Generating " + fileName);
    }

    public void generateServiceFile(String packageName){
        String fileName = entityName + "Service.java";
        System.out.println(timeStamp() + ": Generating " + fileName);

    }

    public void generateServiceImplFile(String packageName){
        String fileName = entityName + "ServiceImpl.java";
        System.out.println(timeStamp() + ": Generating " + fileName);
    }
    public void generateControllerFile(String packageName){
        String fileName = entityName + "Controller.java";
        System.out.println(timeStamp() + ": Generating " + fileName);
    }
    public void generateRepositoryFile(String packageName){
        String fileName = entityName + "Repository.java";
        System.out.println(timeStamp() + ": Generating " + fileName);
    }

    private String timeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }


}
