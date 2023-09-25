package me.valacritty;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.valacritty.models.Course;
import me.valacritty.models.Instructor;
import me.valacritty.utils.ViewFinder;
import me.valacritty.utils.ViewMap;
import me.valacritty.utils.parsers.CourseParser;
import me.valacritty.utils.parsers.InstructorParser;

import java.net.URL;
import java.util.TreeSet;

public class Main extends Application {
    private static TreeSet<Instructor> instructors;
    private static TreeSet<Course> courses;

    public static void main(String[] args) {
        launch(args);
    }

    public static TreeSet<Instructor> getInstructors() {
        return instructors;
    }

    private void initializeData() {
        InstructorParser parser = InstructorParser.getInstance();
        CourseParser courseParser = CourseParser.getInstance();
        courses = courseParser.parse();
        instructors = parser.parse();
    }

    private String initializeStyles() {
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
        initializeData();
        String styleSheet = initializeStyles();

        Scene scene = new Scene(ViewFinder.loadView(ViewMap.HOME));
        primaryStage.setScene(scene);
        primaryStage.getScene().getStylesheets().add(styleSheet);
        primaryStage.setTitle("Project01");
        primaryStage.show();
    }
}