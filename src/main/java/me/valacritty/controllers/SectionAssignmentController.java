package me.valacritty.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import me.valacritty.extensions.region.TemporalClickableAnchorPane;
import me.valacritty.models.Course;
import me.valacritty.models.Instructor;
import me.valacritty.models.Section;
import me.valacritty.models.TimeRange;
import me.valacritty.models.enums.Campus;
import me.valacritty.models.enums.Day;
import me.valacritty.models.enums.InstructionMethod;
import me.valacritty.models.enums.PartOfTerm;
import me.valacritty.persistence.Configuration;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public class SectionAssignmentController {
    private final ObservableList<Section> sectionData = FXCollections.observableArrayList();
    @FXML
    public Button courseOneButton;
    @FXML
    public Button courseTwoButton;
    @FXML
    public Button courseThreeButton;
    @FXML
    public Label firstNameLabel;
    @FXML
    public Label middleNameLabel;
    @FXML
    public Label lastNameLabel;
    @FXML
    public Label phoneLabel;
    @FXML
    public Label homeCampLabel;
    @FXML
    public Label prefCampsLabel;
    @FXML
    public TableView<Section> sectionsView;
    @FXML
    public TableColumn<Section, Integer> crnCol;
    @FXML
    public TableColumn<Section, Course> courseCol;
    @FXML
    public TableColumn<Section, PartOfTerm> partOfTermCol;
    @FXML
    public TableColumn<Section, Campus> campusCol;
    @FXML
    public TableColumn<Section, InstructionMethod> instructionMethodCol;
    @FXML
    public TableColumn<Section, Day> daysCol;
    @FXML
    public TableColumn<Section, TimeRange> timesCol;
    private Instructor selected;
    private TemporalClickableAnchorPane region;

    public void setUserData(Instructor selected, TemporalClickableAnchorPane region) {
        this.selected = selected;
        this.region = region;
        postInit();
    }

    public void postInit() {
        firstNameLabel.setText(selected.getFirstName());
        middleNameLabel.setText(selected.getMiddleName());
        lastNameLabel.setText(selected.getLastName());
        phoneLabel.setText(selected.getHomePhone());
        homeCampLabel.setText(selected.getHomeCampus().toString());
        prefCampsLabel.setText(setPreferredCampusesText(selected.getPreferredCampuses()));
        try {
            courseOneButton.setText(selected.getSections().get(0).getCourse().getCourseNumber());
        } catch (IndexOutOfBoundsException ignored) {}
        try {
            courseTwoButton.setText(selected.getSections().get(1).getCourse().getCourseNumber());
        } catch (IndexOutOfBoundsException ignored) {}
        try {
            courseThreeButton.setText(selected.getSections().get(2).getCourse().getCourseNumber());
        } catch (IndexOutOfBoundsException ignored) {}
        if (!selected.isCanTeachSecondCourse()) {
            courseTwoButton.setText("Unavailable");
            courseTwoButton.setDisable(true);
        }
        if (!selected.isCanTeachThirdCourse()) {
            courseThreeButton.setText("Unavailable");
            courseThreeButton.setDisable(true);
        }
        configureTableView();
    }

    private String setPreferredCampusesText(Set<Campus> campuses) {
        StringBuilder sb = new StringBuilder();
        for (Campus campus : campuses) {
            sb.append(campus).append(", ");
        }

        return sb.toString();
    }

    private void configureTableView() {
        crnCol.setCellValueFactory(new PropertyValueFactory<>("crn"));
        courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
        partOfTermCol.setCellValueFactory(new PropertyValueFactory<>("partOfTerm"));
        campusCol.setCellValueFactory(new PropertyValueFactory<>("campus"));
        instructionMethodCol.setCellValueFactory(new PropertyValueFactory<>("instructionMethod"));
        daysCol.setCellValueFactory(new PropertyValueFactory<>("daysOffered"));
        timesCol.setCellValueFactory(new PropertyValueFactory<>("timeRange"));

        sectionData.addAll(Configuration.getSectionManager()
                .stream()
                .filter(section -> {
                    Optional<TimeRange> rangeOptional = Optional.ofNullable(region.getTemporalEntry().getValue().getRange());
                    return rangeOptional.isPresent() && section.getTimeRange() != null && section.getTimeRange().isInRange(rangeOptional.get());
                })
                .filter(section -> section.getDaysOffered().contains(region.getTemporalEntry().getKey()))
                .toList()
        );

        sectionData.addListener((ListChangeListener<Section>) change -> {
            System.out.printf("[section_assignment_controller]: requesting backup...%n");
            Configuration.saveAll();
        });

        sectionsView.setItems(sectionData);
    }

    @FXML
    public void onAssignFirstCourse(ActionEvent actionEvent) {
        assignCourse(courseOneButton);
    }

    @FXML
    public void onAssignSecondCourse(ActionEvent actionEvent) {
        assignCourse(courseTwoButton);
    }

    @FXML
    public void onAssignThirdCourse(ActionEvent actionEvent) {
        assignCourse(courseThreeButton);
    }

    private void assignCourse(Button courseButton) {
        if (sectionsView.getSelectionModel().getSelectedItem() != null) {
            // we know a section is trying to be added
            updateSection(sectionsView.getSelectionModel().getSelectedItem(), courseOneButton);
        } else {
            selected.getSections().stream()
                    .filter(section -> section.getCourse().getCourseNumber().equalsIgnoreCase(courseOneButton.getText()))
                    .findAny()
                    .ifPresent(section -> {
                        removeFromInstructor(section);
                        courseOneButton.setText("Assign Course");
                    });

        }
    }

    private void updateSection(Section selectedSection, Button courseButton) {
        selected.getSections().stream()
                .filter(section -> section.getCourse().getCourseNumber().equalsIgnoreCase(courseButton.getText()))
                .findAny().ifPresent(this::removeFromInstructor);

        addToInstructor(selectedSection);
        courseButton.setText(selectedSection.getCourse().getCourseNumber());
    }

    private void removeFromInstructor(Section section) {
        selected.getSections().remove(section);
        Configuration.getSectionManager().add(section);
        sectionData.add(section);
    }

    private void addToInstructor(Section section) {
        selected.getSections().add(section);
        Configuration.getSectionManager().remove(section);
        sectionData.remove(section);
    }
}
