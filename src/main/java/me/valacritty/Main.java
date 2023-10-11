package me.valacritty;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.valacritty.persistence.Configuration;
import me.valacritty.utils.ViewFinder;
import me.valacritty.utils.ViewMap;

import java.net.URL;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private String loadStyles() {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        String styleSheet = "";
        URL url = getClass().getResource("/styles.css");
        if (url != null) {
            styleSheet = url.toExternalForm();
        } else {
            System.err.println("failed to load stylesheet, perhaps it is missing?");
        }

        return styleSheet;
    }

    @Override
    public void start(Stage primaryStage) {
        Configuration.initializeManagers();

        Scene scene = new Scene(ViewFinder.loadView(ViewMap.HOME));
        primaryStage.setOnCloseRequest(event -> Configuration.saveAll());
        primaryStage.setScene(scene);
        primaryStage.getScene().getStylesheets().add(loadStyles());
        primaryStage.setTitle("Project01");
        primaryStage.show();
    }
}