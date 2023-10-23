package me.valacritty.extensions.region;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import me.valacritty.models.enums.Day;
import me.valacritty.models.enums.TimeOfDay;

import java.util.Map;

public class TemporalClickableAnchorPane extends ClickableAnchorPane {
    private final String validColour = "-fx-background-color: #e3ffe3;";
    private final String invalidColour = "-fx-background-color: #ffe6e6;";
    private final Label timeLabel;
    private final Map.Entry<Day, TimeOfDay> temporalEntry;

    public TemporalClickableAnchorPane(Map.Entry<Day, TimeOfDay> temporalEntry) {
        this.temporalEntry = temporalEntry;
        this.timeLabel = new Label();
        timeLabel.setText(temporalEntry.getValue().getRange().toString());
        timeLabel.setMaxWidth(Double.MAX_VALUE);
        timeLabel.setAlignment(Pos.CENTER);
        timeLabel.setMouseTransparent(true);
        getChildren().add(timeLabel);
        AnchorPane.setTopAnchor(timeLabel, 0d);
        AnchorPane.setBottomAnchor(timeLabel, 0d);
        AnchorPane.setLeftAnchor(timeLabel, 0d);
        AnchorPane.setRightAnchor(timeLabel, 0d);
        invalidate();   // by default
    }

    public Map.Entry<Day, TimeOfDay> getTemporalEntry() {
        return temporalEntry;
    }

    public void validate() {
        this.setStyle(validColour);
    }

    public void invalidate() {
        this.setStyle(invalidColour);
    }

}
