package me.valacritty.controllers;

import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.valacritty.Main;
import me.valacritty.extensions.region.TemporalClickableRegion;
import me.valacritty.models.Course;
import me.valacritty.models.Instructor;
import me.valacritty.models.enums.Campus;
import me.valacritty.models.enums.Day;
import me.valacritty.models.enums.Rank;
import me.valacritty.models.enums.TimeOfDay;
import me.valacritty.persistence.Configuration;
import me.valacritty.utils.ViewFinder;
import me.valacritty.utils.ViewMap;

import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    private final ObservableList<Instructor> instructorData = FXCollections.observableArrayList();
    private final ObservableList<Course> courseData = FXCollections.observableArrayList();
    private final ObservableList<Campus> campusData = FXCollections.observableArrayList();
    private final String validColour = "-fx-background-color: #e3ffe3;";
    private final String invalidColour = "-fx-background-color: #ffe6e6;";
    @FXML
    public AnchorPane homePane;
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
    public VBox availabilitiesBox;
    @FXML
    public GridPane availabilitiesGrid;
    @FXML
    public Label selectProfessorLabel;
    @FXML
    public TextField queryField;
    private HashSet<TemporalClickableRegion> temporalRegions;

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

        // initialize availabilities grid
        initializeAvailabilitiesGrid();

    }

    private void initializeAvailabilitiesGrid() {
        temporalRegions = new HashSet<>(availabilitiesGrid.getColumnCount() * availabilitiesGrid.getRowCount());

        for (int rowIdx = 0; rowIdx < availabilitiesGrid.getColumnCount(); rowIdx++) {
            for (int colIdx = 0; colIdx < availabilitiesGrid.getRowCount(); colIdx++) {
                Map.Entry<Day, TimeOfDay> entry = mapToDayAndTimeOfDay(rowIdx, colIdx);
                TemporalClickableRegion region = new TemporalClickableRegion(entry);
                if (entry.getValue() == TimeOfDay.DEFAULT)
                    continue;
                temporalRegions.add(region);
                availabilitiesGrid.add(region, rowIdx, colIdx);
            }
        }
    }

    private Map.Entry<Day, TimeOfDay> mapToDayAndTimeOfDay(int rowIdx, int colIdx) {
        Day day = null;
        TimeOfDay timeOfDay;

        switch (colIdx) {
            case 0 -> timeOfDay = TimeOfDay.EARLY_MORNING;
            case 1 -> timeOfDay = TimeOfDay.MORNING;
            case 2 -> timeOfDay = TimeOfDay.EARLY_AFTERNOON;
            case 3 -> timeOfDay = TimeOfDay.AFTERNOON;
            case 4 -> timeOfDay = TimeOfDay.EVENING;
            default -> timeOfDay = TimeOfDay.DEFAULT;
        }

        switch (rowIdx) {
            case 0 -> day = Day.SUNDAY;
            case 1 -> day = Day.MONDAY;
            case 2 -> day = Day.TUESDAY;
            case 3 -> day = Day.WEDNESDAY;
            case 4 -> day = Day.THURSDAY;
            case 5 -> day = Day.FRIDAY;
            case 6 -> day = Day.SATURDAY;
        }

        return new AbstractMap.SimpleEntry<>(day, timeOfDay);
    }

    @FXML
    public void onSearch() {
        String query = queryField.getText().toLowerCase().trim();
        instructorData.clear(); // Clear the existing data
        instructorData.addAll(Configuration.getInstructorManager().matches(instructor -> instructor.getId().contains(query)
                || instructor.getFirstName().toLowerCase().contains(query)
                || instructor.getLastName().toLowerCase().contains(query)));
    }

    private void setCellSelectionListener() {
        instructorView.setOnMouseClicked(event -> {
            Instructor selected = instructorView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // only make updates when an instructor is actually selected
                updateAvailabilities(selected);
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

    private void updateAvailabilities(Instructor selected) {
        availabilitiesGrid.getChildren().stream()
                .filter(node -> node instanceof TemporalClickableRegion)
                .map(node -> (TemporalClickableRegion) node)
                .forEach(region -> {
                    // process the nodes so they include the correct information
                    Map.Entry<Day, TimeOfDay> regionEntry = region.getTemporalEntry();
                    region.setOnMouseClicked(event -> handleRegionMouseClick(selected, region));
                    if (selected.getAvailabilities().entrySet().stream()
                            .anyMatch(entry -> entry.getKey() == regionEntry.getKey() && entry.getValue().contains(regionEntry.getValue()))) {
                        region.validate();
                        region.setDisable(false);
                    } else {
                        region.invalidate();
                        region.setDisable(true);
                    }
                });
    }

    private void handleRegionMouseClick(Instructor selected, TemporalClickableRegion region) {
        FXMLLoader loader = ViewFinder.getLoaderFrom(ViewMap.COURSE_ASSIGNMENT);
        Parent root;
        try {
            root = loader.load();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CourseAssignmentController controller = loader.getController();
        controller.setInstructor(selected);
        controller.setRegion(region);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initOwner(Main.getStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
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
