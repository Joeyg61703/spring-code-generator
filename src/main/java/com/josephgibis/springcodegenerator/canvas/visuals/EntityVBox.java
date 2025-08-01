package com.josephgibis.springcodegenerator.canvas.visuals;

import com.josephgibis.springcodegenerator.canvas.CanvasEntity;
import com.josephgibis.springcodegenerator.canvas.CanvasManager;
import com.josephgibis.springcodegenerator.canvas.EntityProperty;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class EntityVBox extends VBox {
    private CanvasEntity entity;
    private VBox propertiesBox;
    private double dragStartX, dragStartY;
    private Pane canvas;

    public EntityVBox(CanvasEntity entity, Pane canvas) {
        this.entity = entity;
        this.canvas = canvas;
        initializeNode();
        setupDragHandling();
        updateDisplay();
    }

    private void initializeNode() {
          setMaxWidth(200);
          setMinWidth(200);

        propertiesBox = new VBox();
        propertiesBox.getStyleClass().add("entity-properties");
        getChildren().addAll(createHeader(), propertiesBox);

    }

    private VBox createHeader() {
        VBox header = new VBox();
        header.getStyleClass().add("entity-header");
        header.getChildren().add(createTitle());
        return header;
    }

    private Label createTitle(){
        Label titleLabel = new Label();
        titleLabel.getStyleClass().add("entity-title");
        titleLabel.textProperty().bind(entity.nameProperty());
        return titleLabel;
    }

    private void setupDragHandling() {
        setOnMousePressed(this::handleMousePressed);
        setOnMouseDragged(this::handleMouseDragged);
    }

    private void handleMousePressed(MouseEvent event) {
        CanvasManager.setSelectedEntity(this.entity);
        dragStartX = event.getSceneX() - getLayoutX();
        dragStartY = event.getSceneY() - getLayoutY();
        toFront();
    }

    private void handleMouseDragged(MouseEvent event) {
        double newX = event.getSceneX() - dragStartX;
        double newY = event.getSceneY() - dragStartY;

        if (newX < 0){
            newX = 0;
        }

        if (newX > canvas.getWidth() - getWidth()) {
            newX = canvas.getWidth() - getWidth();
        }

        if (newY < 0){
            newY = 0;
        }

        if (newY > canvas.getHeight() - getHeight()) {
            newY = canvas.getHeight() - getHeight();
        }

        setLayoutX(newX);
        setLayoutY(newY);

        entity.setX(newX);
        entity.setY(newY);
    }

    public void updateDisplay() {
        propertiesBox.getChildren().clear();

        Label idProperty = new Label("id: " + entity.getIdType());
        propertiesBox.getChildren().add(idProperty);

        //TODO: show relationship foreign id?

        for (EntityProperty property : entity.getProperties()) {
            String propertyText = property.getName() + ": " + property.getType();
            Label propertyLabel = new Label(propertyText);
            propertiesBox.getChildren().add(propertyLabel);
        }
    }

    public CanvasEntity getEntity() { return entity; }
    public void setEntity(CanvasEntity entity) {this.entity = entity;}
}