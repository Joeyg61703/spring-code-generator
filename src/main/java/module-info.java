module com.josephgibis.springcodegenerator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.josephgibis.springcodegenerator to javafx.fxml;
    exports com.josephgibis.springcodegenerator;
}