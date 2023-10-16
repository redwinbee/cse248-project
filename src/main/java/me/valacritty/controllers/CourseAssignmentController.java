package me.valacritty.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.valacritty.extensions.region.TemporalClickableRegion;
import me.valacritty.models.Course;
import me.valacritty.models.Instructor;
import me.valacritty.models.Section;
import me.valacritty.persistence.Configuration;
import me.valacritty.utils.ViewFinder;
import me.valacritty.utils.ViewMap;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class CourseAssignmentController {
    @FXML
    public Button secondCourseButton;
    @FXML
    public Button thirdCourseButton;
    @FXML
    public Button firstCourseButton;
    private TemporalClickableRegion region;
    private Instructor selected;

    public void setInstructor(Instructor selected) {
        this.selected = selected;
        postInit();
    }

    public void setRegion(TemporalClickableRegion region) {
        this.region = region;
    }

    private void postInit() {
        initializeAssignmentAvail();
    }

    private void initializeAssignmentAvail() {
        Iterator<Course> itr = selected.getCourses().iterator();
        if (itr.hasNext()) {
            firstCourseButton.setText(itr.next().getCourseNumber());
        } else {
            firstCourseButton.setText("+ Assign Course");
        }

        if (!selected.isCanTeachSecondCourse()) {
            secondCourseButton.setText("Unavailable");
            secondCourseButton.setDisable(true);
        } else {
            if (itr.hasNext()) {
                secondCourseButton.setText(itr.next().getCourseNumber());
            } else {
                secondCourseButton.setText("+ Assign Course");
            }
        }

        if (!selected.isCanTeachThirdCourse()) {
            thirdCourseButton.setText("Unavailable");
            thirdCourseButton.setDisable(true);
        } else {
            if (itr.hasNext()) {
                thirdCourseButton.setText(itr.next().getCourseNumber());
            } else {
                thirdCourseButton.setText("+ Assign Course");
            }
        }
    }

    private Set<Section> getContextualSections() {
        return Configuration.getSectionManager().stream()
                .filter(section -> {
                    for (Course course : selected.getCourses()) {
                        return section.getCourse().getCourseNumber().equalsIgnoreCase(course.getCourseNumber());
                    }
                    return false;
                })
                .collect(Collectors.toSet());
    }

    private void openPossibleCourses(Set<Section> possibleSections) {
        FXMLLoader loader = ViewFinder.getLoaderFrom(ViewMap.POSSIBLE_SECTIONS);
        Parent root;
        try {
            root = loader.load();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PossibleSectionsController controller = loader.getController();
        controller.setSections(possibleSections);
        controller.setInstructor(selected);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        controller.setStage(stage);
        stage.setScene(scene);
        stage.initOwner(null);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
        initializeAssignmentAvail();
    }

    @FXML
    public void onAssignFirstCourse(ActionEvent actionEvent) {
        Set<Section> availSections = getContextualSections();
        openPossibleCourses(availSections);
    }

    @FXML
    public void onAssignSecondCourse(ActionEvent actionEvent) {
    }

    @FXML
    public void onAssignThirdCourse(ActionEvent actionEvent) {

    }
}
