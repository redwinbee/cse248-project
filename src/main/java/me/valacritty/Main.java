package me.valacritty;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.valacritty.models.Instructor;
import me.valacritty.utils.Parser;
import me.valacritty.utils.ViewFinder;
import me.valacritty.utils.ViewMap;

import java.util.LinkedList;

public class Main extends Application {
    private static LinkedList<Instructor> instructors;

    public static void main(String[] args) {
        launch(args);
    }

    public static LinkedList<Instructor> getInstructors() {
        return instructors;
    }

    private static void deserializeData() {
        Parser parser = Parser.getInstance();
        instructors = parser.parse();
    }

    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        Parent parent = ViewFinder.loadView(ViewMap.HOME);
        deserializeData();
        Scene scene = new Scene(parent);
        primaryStage.setTitle("Project01");
        primaryStage.setScene(scene);
        String styleSheet = getClass().getResource("/styles.css").toExternalForm();
        primaryStage.getScene().getStylesheets().add(styleSheet);
        primaryStage.show();
    }
}