package me.valacritty.utils;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

public class ViewFinder {
    public static <T> T loadView(String fxmlName) {
        URL fxmlPath = getFxmlPath(fxmlName);

        try {
            FXMLLoader loader = new FXMLLoader(fxmlPath);
            return loader.load();
        } catch (IOException ex) {
            System.err.println("[err] an error occurred during FXML loading: " + ex.getMessage());
            return null;
        }
    }

    private static URL getFxmlPath(String fxmlName) {
        URL fxmlPath = ViewMap.class.getResource("/fxml/" + fxmlName);
        if (fxmlPath == null) {
            throw new IllegalArgumentException("[err] FXML mapping not found for: " + fxmlName);
        }
        return fxmlPath;
    }

    public static FXMLLoader getLoaderFrom(String fxmlName) {
        URL fxmlPath = getFxmlPath(fxmlName);
        return new FXMLLoader(fxmlPath);
    }
}
