module com.josephgibis.springcodegenerator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires freemarker;


    opens com.josephgibis.springcodegenerator to javafx.fxml;
    exports com.josephgibis.springcodegenerator;
    exports com.josephgibis.springcodegenerator.canvas;
    opens com.josephgibis.springcodegenerator.canvas to javafx.fxml;
    exports com.josephgibis.springcodegenerator.canvas.enums;
    opens com.josephgibis.springcodegenerator.canvas.enums to javafx.fxml;
    exports com.josephgibis.springcodegenerator.controllers;
    opens com.josephgibis.springcodegenerator.controllers to javafx.fxml;
    exports com.josephgibis.springcodegenerator.canvas.visuals;
    opens com.josephgibis.springcodegenerator.canvas.visuals to javafx.fxml;
}