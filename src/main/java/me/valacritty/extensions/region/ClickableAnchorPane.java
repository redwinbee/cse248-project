package me.valacritty.extensions.region;

import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public abstract class ClickableAnchorPane extends AnchorPane {
    public ClickableAnchorPane() {
        setOnMouseEntered(event -> {
            setCursor(Cursor.HAND);
            setEffect(new DropShadow(10, Color.GRAY));
        });

        setOnMouseExited(event -> {
            setCursor(Cursor.DEFAULT);
            setEffect(null);
        });
    }
}
