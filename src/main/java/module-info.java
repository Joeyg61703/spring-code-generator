module com.josephgibis.springcodegenerator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.josephgibis.springcodegenerator to javafx.fxml;
    exports com.josephgibis.springcodegenerator;
}