package me.valacritty.extensions.region;

import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public abstract class ClickableRegion extends Region {
    public ClickableRegion() {
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
