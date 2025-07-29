package com.josephgibis.springcodegenerator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("app.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Spring Code Generator");

        String css = this.getClass().getResource("css/styles.css").toExternalForm();

        Image logo = new Image(getClass().getResourceAsStream("assets/logo.png"));
        scene.getStylesheets().add(css);
        stage.getIcons().add(logo);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}