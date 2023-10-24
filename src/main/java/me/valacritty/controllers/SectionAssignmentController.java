package me.valacritty.controllers;

import javafx.collections.FXCollections;
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
        sectionsView.setItems(sectionData);
    }

    @FXML
    public void onAssignFirstCourse(ActionEvent actionEvent) {
        assignSection(0, courseOneButton, "Assign Course #1");
    }

    @FXML
    public void onAssignSecondCourse(ActionEvent actionEvent) {
        assignSection(1, courseTwoButton, "Assign Course #2");
    }

    @FXML
    public void onAssignThirdCourse(ActionEvent actionEvent) {
        assignSection(2, courseThreeButton, "Assign Course #2");
    }

    private void assignSection(int index, Button button, String failureText) {
        Section curr = null;
        Section selectedSection = sectionsView.getSelectionModel().getSelectedItem();
        if (selectedSection == null) {
            return;
        }

        try {
            curr = selected.getSections().get(index);
        } catch (IndexOutOfBoundsException ignored) {

        }

        if (curr != null) {
            sectionData.add(curr);
            Configuration.getSectionManager().add(curr);
            selected.getSections().remove(index);
            selected.getSections().add(index, selectedSection);
            sectionData.remove(selectedSection);
            Configuration.getSectionManager().remove(selectedSection);
            try {
                button.setText(selected.getSections().get(index).getCourse().getCourseNumber());
            } catch (NullPointerException ex) {
                button.setText(failureText);
            }
        } else {
            sectionData.remove(selectedSection);
            Configuration.getSectionManager().remove(selectedSection);
            try {
                button.setText(selectedSection.getCourse().getCourseNumber());
                selected.getSections().add(index, selectedSection);
            } catch (IndexOutOfBoundsException ex) {
                selected.getSections().add(selectedSection);
            }
        }
    }
}
