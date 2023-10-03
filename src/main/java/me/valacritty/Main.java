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
import me.valacritty.utils.parsers.InstructorRecentCoursesParser;

import java.net.URL;
import java.util.Collection;
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

    public static TreeSet<Course> getCourses() {
        return courses;
    }

    private void initializeData() {
        InstructorParser parser = InstructorParser.getInstance("Instructors.csv");
        CourseParser courseParser = CourseParser.getInstance("CourseInformation.csv");
        InstructorRecentCoursesParser instructorRecentCoursesParser =
                InstructorRecentCoursesParser.getInstance("Instructor_Recent_Courses.csv");

        Collection<Instructor> instructorsWithRecentCourses = instructorRecentCoursesParser.parse();
        courses = new TreeSet<>(courseParser.parse());
        instructors = new TreeSet<>(parser.parse());

        instructors.addAll(instructorsWithRecentCourses);
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