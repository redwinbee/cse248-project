package me.valacritty.extensions.region;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import me.valacritty.models.enums.Day;
import me.valacritty.models.enums.TimeOfDay;

import java.util.Map;

public class TemporalClickableRegion extends ClickableRegion {
    private final String validColour = "-fx-background-color: #e3ffe3;";
    private final String invalidColour = "-fx-background-color: #ffe6e6;";
    private final Label timeLabel;
    private final Map.Entry<Day, TimeOfDay> temporalEntry;

    public TemporalClickableRegion(Map.Entry<Day, TimeOfDay> temporalEntry) {
        this.temporalEntry = temporalEntry;
        this.timeLabel = new Label();
        timeLabel.setText(temporalEntry.getValue().getRange().toString());
        timeLabel.setMaxWidth(Double.MAX_VALUE);
        timeLabel.setAlignment(Pos.CENTER);
        timeLabel.setMouseTransparent(true);
        getChildren().add(timeLabel);
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
