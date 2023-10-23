package me.valacritty;

import atlantafx.base.theme.NordLight;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.valacritty.persistence.Configuration;
import me.valacritty.utils.ViewFinder;
import me.valacritty.utils.ViewMap;

import java.net.URL;

public class Main extends Application {
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return primaryStage;
    }


    private String loadStyles() {
        Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());

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
        //Configuration.initializeGenerators(0);
        Main.primaryStage = primaryStage;

        Scene scene = new Scene(ViewFinder.loadView(ViewMap.HOME));
        primaryStage.setOnCloseRequest(event -> Configuration.saveAll());
        primaryStage.setScene(scene);
        primaryStage.getScene().getStylesheets().add(loadStyles());
        primaryStage.setTitle("Project01");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}