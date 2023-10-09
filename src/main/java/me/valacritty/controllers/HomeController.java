package me.valacritty.controllers;

import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.valacritty.Main;
import me.valacritty.models.Course;
import me.valacritty.models.Instructor;
import me.valacritty.models.enums.Campus;
import me.valacritty.models.enums.Day;
import me.valacritty.models.enums.Rank;
import me.valacritty.models.enums.TimeOfDay;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class HomeController implements Initializable {
    private final ObservableList<Instructor> instructorData = FXCollections.observableArrayList();
    private final ObservableList<Course> courseData = FXCollections.observableArrayList();
    private final ObservableList<Campus> campusData = FXCollections.observableArrayList();
    private final String validColour = "-fx-background-color: #e3ffe3;";
    private final String invalidColour = "-fx-background-color: #ffe6e6;";
    @FXML
    public TableView<Instructor> instructorView;
    @FXML
    public TableColumn<Instructor, String> idCol;
    @FXML
    public TableColumn<Instructor, String> firstNameCol;
    @FXML
    public TableColumn<Instructor, String> middleNameCol;
    @FXML
    public TableColumn<Instructor, String> lastNameCol;
    @FXML
    public TableColumn<Instructor, Rank> rankCol;
    @FXML
    public TableColumn<Instructor, Campus> homeCampusCol;
    @FXML
    public TableColumn<Instructor, Boolean> onlCertifiedCol;
    @FXML
    public TableColumn<Instructor, Boolean> secondCourseCol;
    @FXML
    public TableColumn<Instructor, Boolean> thirdCourseCol;
    @FXML
    public Region sunEarlyMorning;
    @FXML
    public Region monEarlyMorning;
    @FXML
    public Region tueEarlyMorning;
    @FXML
    public Region wedEarlyMorning;
    @FXML
    public Region thurEarlyMorning;
    @FXML
    public Region friEarlyMorning;
    @FXML
    public Region satEarlyMorning;
    @FXML
    public Region sunMorning;
    @FXML
    public Region monMorning;
    @FXML
    public Region tueMorning;
    @FXML
    public Region wedMorning;
    @FXML
    public Region thurMorning;
    @FXML
    public Region friMorning;
    @FXML
    public Region satMorning;
    @FXML
    public Region sunEarlyAfternoon;
    @FXML
    public Region monEarlyAfternoon;
    @FXML
    public Region tueEarlyAfternoon;
    @FXML
    public Region wedEarlyAfternoon;
    @FXML
    public Region thurEarlyAfternoon;
    @FXML
    public Region friEarlyAfternoon;
    @FXML
    public Region satEarlyAfternoon;
    @FXML
    public Region sunAfternoon;
    @FXML
    public Region monAfternoon;
    @FXML
    public Region tueAfternoon;
    @FXML
    public Region wedAfternoon;
    @FXML
    public Region thurAfternoon;
    @FXML
    public Region friAfternoon;
    @FXML
    public Region satAfternoon;
    @FXML
    public Region sunEvening;
    @FXML
    public Region monEvening;
    @FXML
    public Region tueEvening;
    @FXML
    public Region wedEvening;
    @FXML
    public Region thurEvening;
    @FXML
    public Region friEvening;
    @FXML
    public Region satEvening;
    @FXML
    public VBox availabilitiesBox;
    @FXML
    public GridPane availabilitiesGrid;
    @FXML
    public Label selectProfessorLabel;
    @FXML
    public Label courseTitleLabel;
    @FXML
    public TextField queryField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // configure cell value factories
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middleNameCol.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));
        homeCampusCol.setCellValueFactory(new PropertyValueFactory<>("homeCampus"));
        onlCertifiedCol.setCellValueFactory(new PropertyValueFactory<>("canTeachOnline"));
        secondCourseCol.setCellValueFactory(new PropertyValueFactory<>("canTeachSecondCourse"));
        thirdCourseCol.setCellValueFactory(new PropertyValueFactory<>("canTeachThirdCourse"));

        // configure cell value listeners
        setCellSelectionListener();

        // set the view to the observable list
        instructorView.setItems(instructorData);
        Styles.toggleStyleClass(instructorView, Styles.STRIPED);

    }

    @FXML
    public void onSearch() {
        String query = queryField.getText().toLowerCase().trim();
        instructorData.clear(); // Clear the existing data

        Main.getInstructors().stream()
                .filter(instructor -> instructor.getId().contains(query)
                        || instructor.getFirstName().toLowerCase().contains(query)
                        || instructor.getLastName().toLowerCase().contains(query))
                .forEach(instructorData::add);
    }

    private void setCellSelectionListener() {
        instructorView.setOnMouseClicked(event -> {
            clearAvailabilitiesGrid();
            Instructor selected = instructorView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // only make updates when an instructor is actually selected
                updateAvailabilitiesGrid(selected);
                updateContentVisibility(true);

                // Update the courses and campuses ListViews
                courseData.clear();
                courseData.addAll(selected.getCourses());

                campusData.clear();
                campusData.addAll(selected.getPreferredCampuses());

                return;
            }

            updateContentVisibility(false);
        });
    }

    private void updateAvailabilitiesGrid(Instructor selected) {
        applyAvailabilityColour(selected.getAvailableEarlyMornings(), TimeOfDay.EARLY_MORNING);
        applyAvailabilityColour(selected.getAvailableMornings(), TimeOfDay.MORNING);
        applyAvailabilityColour(selected.getAvailableEarlyAfternoons(), TimeOfDay.EARLY_AFTERNOON);
        applyAvailabilityColour(selected.getAvailableAfternoons(), TimeOfDay.AFTERNOON);
        applyAvailabilityColour(selected.getAvailableEvenings(), TimeOfDay.EVENING);
        applyAvailabilityColour(selected.getAvailableWeekends(), TimeOfDay.IRRELEVANT);
    }

    private void clearAvailabilitiesGrid() {
        for (Node node : availabilitiesGrid.getChildren().filtered(node -> !(node instanceof Label))) {
            if (node instanceof StackPane pane) {
                pane.getChildren()
                        .filtered(child -> !(child instanceof Label))
                        .forEach(child -> child.setStyle(invalidColour));
            } else {
                node.setStyle(invalidColour);
            }
        }
    }

    private void applyAvailabilityColour(Set<Day> availableDays, TimeOfDay timeOfDay) {
        switch (timeOfDay) {
            case EARLY_MORNING ->
                    applyStyleToRegions(availableDays, sunEarlyMorning, monEarlyMorning, tueEarlyMorning, wedEarlyMorning, thurEarlyMorning, friEarlyMorning, satEarlyMorning);
            case MORNING ->
                    applyStyleToRegions(availableDays, sunMorning, monMorning, tueMorning, wedMorning, thurMorning, friMorning, satMorning);
            case EARLY_AFTERNOON ->
                    applyStyleToRegions(availableDays, sunEarlyAfternoon, monEarlyAfternoon, tueEarlyAfternoon, wedEarlyAfternoon, thurEarlyAfternoon, friEarlyAfternoon, satEarlyAfternoon);
            case AFTERNOON ->
                    applyStyleToRegions(availableDays, sunAfternoon, monAfternoon, tueAfternoon, wedAfternoon, thurAfternoon, friAfternoon, satAfternoon);
            case EVENING ->
                    applyStyleToRegions(availableDays, sunEvening, monEvening, tueEvening, wedEvening, thurEvening, friEvening, satEvening);

            // TODO it's possible this is wrong because the original file doesn't specify what time of day weekend instructors are available
            case IRRELEVANT -> {
                for (Day day : availableDays) {
                    switch (day) {
                        case SATURDAY -> {
                            satEarlyMorning.setStyle(validColour);
                            satMorning.setStyle(validColour);
                            satEarlyAfternoon.setStyle(validColour);
                            satAfternoon.setStyle(validColour);
                            satEvening.setStyle(validColour);
                        }
                        case SUNDAY -> {
                            sunEarlyMorning.setStyle(validColour);
                            sunMorning.setStyle(validColour);
                            sunEarlyAfternoon.setStyle(validColour);
                            sunAfternoon.setStyle(validColour);
                            sunEvening.setStyle(validColour);
                        }
                    }
                }
            }
        }
    }

    private void applyStyleToRegions(Set<Day> availableDays, Region sunRegion, Region monRegion, Region tueRegion, Region wedRegion, Region thurRegion, Region friRegion, Region satRegion) {
        for (Day day : availableDays) {
            switch (day) {
                case SUNDAY -> sunRegion.setStyle(validColour);
                case MONDAY -> monRegion.setStyle(validColour);
                case TUESDAY -> tueRegion.setStyle(validColour);
                case WEDNESDAY -> wedRegion.setStyle(validColour);
                case THURSDAY -> thurRegion.setStyle(validColour);
                case FRIDAY -> friRegion.setStyle(validColour);
                case SATURDAY -> satRegion.setStyle(validColour);
            }
        }
    }

    private void updateContentVisibility(boolean contentAvailable) {
        if (contentAvailable) {
            availabilitiesBox.setVisible(true);
            selectProfessorLabel.setVisible(false);
        } else {
            availabilitiesBox.setVisible(false);
            selectProfessorLabel.setVisible(true);
        }
    }
}
