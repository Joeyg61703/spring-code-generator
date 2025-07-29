module com.josephgibis.springcodegenerator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires freemarker;


    opens com.josephgibis.springcodegenerator to javafx.fxml;
    exports com.josephgibis.springcodegenerator;
    exports com.josephgibis.springcodegenerator.model;
    opens com.josephgibis.springcodegenerator.model to javafx.fxml;
    exports com.josephgibis.springcodegenerator.model.enums;
    opens com.josephgibis.springcodegenerator.model.enums to javafx.fxml;
    exports com.josephgibis.springcodegenerator.controllers;
    opens com.josephgibis.springcodegenerator.controllers to javafx.fxml;
}