package com.josephgibis.springcodegenerator.canvas.visuals;

import com.josephgibis.springcodegenerator.canvas.EntityRelationship;
import com.josephgibis.springcodegenerator.canvas.enums.RelationshipType;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.layout.Pane;

public class RelationshipLine extends Pane {
    private final EntityRelationship relationship;
    private final EntityVBox sourceVBox;
    private final EntityVBox targetVBox;
    private final Line line;
    private final Label relationshipLabel;

    public RelationshipLine(EntityRelationship relationship, EntityVBox sourceVBox, EntityVBox targetVBox) {
        this.relationship = relationship;
        this.sourceVBox = sourceVBox;
        this.targetVBox = targetVBox;

        line = new Line();
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(2);

        relationshipLabel = createRelationshipLabel();

        getChildren().addAll(line, relationshipLabel);

        bindLineToEntities();
        updateRelationshipLabel();

        Platform.runLater(this::updateLabelPosition); //sets the label to the center immediately
    }

    private Label createRelationshipLabel() {
        Label label = new Label();
        label.setTextFill(Color.WHITE);
        label.setStyle(
                "-fx-background-color: black; " +
                "-fx-background-radius: 8; " +
                "-fx-padding: 2 8 2 8; "

                );
        return label;
    }

    private void bindLineToEntities() {
        // Line starts at center right of source VBox
        line.startXProperty().bind(
                sourceVBox.layoutXProperty().add(sourceVBox.widthProperty())
        );
        line.startYProperty().bind(
                sourceVBox.layoutYProperty().add(sourceVBox.heightProperty().divide(2))
        );

        line.endXProperty().bind(targetVBox.layoutXProperty());
        line.endYProperty().bind(
                targetVBox.layoutYProperty().add(targetVBox.heightProperty().divide(2))
        );

        line.endXProperty().addListener((obs, oldVal, newVal) -> updateLabelPosition());
        line.endYProperty().addListener((obs, oldVal, newVal) -> updateLabelPosition());
        line.startXProperty().addListener((obs, oldVal, newVal) -> updateLabelPosition());
        line.startYProperty().addListener((obs, oldVal, newVal) -> updateLabelPosition());
    }

    private void updateLabelPosition() {
        double startX = line.getStartX();
        double startY = line.getStartY();
        double endX = line.getEndX();
        double endY = line.getEndY();
        double centerX = (startX + endX) / 2;
        double centerY = (startY + endY) / 2;

        // Offset to center
        relationshipLabel.setLayoutX(centerX - 10);
        relationshipLabel.setLayoutY(centerY - 10);
    }

    private void updateRelationshipLabel() {
        RelationshipType type = relationship.getRelationshipType();

        String labelText = switch (type) {
            case ONE_TO_ONE -> "1 -> 1";
            case ONE_TO_MANY -> "1 -> M";
            case MANY_TO_ONE -> "M -> 1";
            case MANY_TO_MANY -> "M -> M";
        };

        relationshipLabel.setText(labelText);
    }

    public EntityRelationship getRelationship() {
        return relationship;
    }
}