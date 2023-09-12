package me.valacritty;

import atlantafx.base.theme.PrimerLight;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.valacritty.models.Instructor;
import me.valacritty.utils.ViewFinder;
import me.valacritty.utils.ViewMap;

import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

public class Main extends Application {
    private static TreeSet<Instructor> instructors;

    public static void main(String[] args) {
        launch(args);
    }

    public static TreeSet<Instructor> getInstructors() {
        return instructors;
    }

    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        Parent parent = ViewFinder.loadView(ViewMap.HOME);
        Scene scene = new Scene(parent);
        primaryStage.setTitle("Project01");
        primaryStage.setScene(scene);
        String styleSheet = getClass().getResource("/styles.css").toExternalForm();
        primaryStage.getScene().getStylesheets().add(styleSheet);
        deserializeData();
        primaryStage.show();
    }

    private void deserializeData() {
        // register our custom deserializer
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Instructor.class, new InstructorDeserializer());
        mapper.registerModule(module);

        // deserialize
        try {
            instructors = mapper.readValue(new File("instructors.json"), new TypeReference<>() {
            });
            for (Instructor ins : instructors) {
                System.out.println(ins.toString());
            }
        } catch (IOException e) {
            System.err.println("[err]: an error occurred while parsing JSON: " + e.getMessage());
        }
    }
}