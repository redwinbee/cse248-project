package me.valacritty.controllers;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.layout.ModalBox;
import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import me.valacritty.extensions.region.TemporalClickableAnchorPane;
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
    private final ModalBox assignmentBox = new ModalBox();
    private final ModalPane assignmentPane = new ModalPane();
    @FXML
    public AnchorPane rootPane;
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
    private HashSet<TemporalClickableAnchorPane> temporalRegions;

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

        postInitialization();
    }

    private void postInitialization() {
        // set the pane up
        rootPane.getChildren().add(assignmentPane);
        assignmentPane.setPersistent(true);
        AnchorPane.setTopAnchor(assignmentPane, 0d);
        AnchorPane.setBottomAnchor(assignmentPane, 0d);
        AnchorPane.setLeftAnchor(assignmentPane, 0d);
        AnchorPane.setRightAnchor(assignmentPane, 0d);

        // set the box up
        assignmentBox.setMaxSize(500, 500);
        assignmentBox.setOnClose(event -> assignmentPane.setDisplay(false));
    }

    private void initializeAvailabilitiesGrid() {
        temporalRegions = new HashSet<>(availabilitiesGrid.getColumnCount() * availabilitiesGrid.getRowCount());

        for (int rowIdx = 0; rowIdx < availabilitiesGrid.getColumnCount(); rowIdx++) {
            for (int colIdx = 0; colIdx < availabilitiesGrid.getRowCount(); colIdx++) {
                Map.Entry<Day, TimeOfDay> entry = mapToDayAndTimeOfDay(rowIdx, colIdx);
                TemporalClickableAnchorPane region = new TemporalClickableAnchorPane(entry);
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
                return;
            }

            updateContentVisibility(false);
        });
    }

    private void updateAvailabilities(Instructor selected) {
        availabilitiesGrid.getChildren().stream()
                .filter(node -> node instanceof TemporalClickableAnchorPane)
                .map(node -> (TemporalClickableAnchorPane) node)
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

    private void handleRegionMouseClick(Instructor selected, TemporalClickableAnchorPane region) {
        showAssignmentPane(selected, region);
    }

    private void showAssignmentPane(Instructor selected, TemporalClickableAnchorPane region) {
        assignmentBox.getChildren().removeIf(node -> node instanceof AnchorPane);
        FXMLLoader loader = ViewFinder.getLoaderFrom(ViewMap.SECTION_ASSIGNMENT);
        Parent root = new AnchorPane();
        try {
            root = loader.load();
        } catch (IOException e) {
            System.err.println("[err] an error occurred while loading the assignment pane node: " + e.getMessage());
        }

        SectionAssignmentController controller = loader.getController();
        controller.setUserData(selected, region);
        assignmentBox.addContent(root);
        AnchorPane.setTopAnchor(root, 0d);
        AnchorPane.setBottomAnchor(root, 0d);
        AnchorPane.setLeftAnchor(root, 0d);
        AnchorPane.setRightAnchor(root, 0d);
        assignmentPane.show(assignmentBox);
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
