package me.valacritty.controllers;

import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;
import me.valacritty.models.Course;
import me.valacritty.models.Instructor;
import me.valacritty.models.Section;
import me.valacritty.models.TimeRange;
import me.valacritty.models.enums.Campus;
import me.valacritty.models.enums.Day;
import me.valacritty.models.enums.InstructionMethod;
import me.valacritty.models.enums.PartOfTerm;

import java.util.Set;

public class PossibleSectionsController {
    private final ObservableList<Section> sectionData = FXCollections.observableArrayList();
    @FXML
    public Button deleteButton;
    private Stage stage;
    @FXML
    public TableView<Section> sectionsView;
    @FXML
    public TableColumn<Section, Integer> crnCol;
    @FXML
    public TableColumn<Section, Course> courseCol;
    @FXML
    public TableColumn<Section, PartOfTerm> potCol;
    @FXML
    public TableColumn<Section, Campus> campusCol;
    @FXML
    public TableColumn<Section, InstructionMethod> insMethodCol;
    @FXML
    public TableColumn<Section, Day> daysCol;
    @FXML
    public TableColumn<Section, TimeRange> timeRangeCol;
    private Set<Section> possibleSections;
    private Instructor instructor;

    public void initialize() {
        deleteButton.getStyleClass().add(Styles.DANGER);
    }

    public void setSections(Set<Section> possibleSections) {
        this.possibleSections = possibleSections;
        initializeListView();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    private void initializeListView() {
        crnCol.setCellValueFactory(new PropertyValueFactory<>("crn"));
        courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
        potCol.setCellValueFactory(new PropertyValueFactory<>("partOfTerm"));
        campusCol.setCellValueFactory(new PropertyValueFactory<>("campus"));
        insMethodCol.setCellValueFactory(new PropertyValueFactory<>("instructionMethod"));
        daysCol.setCellValueFactory(new PropertyValueFactory<>("daysOffered"));
        timeRangeCol.setCellValueFactory(new PropertyValueFactory<>("timeRange"));
        sectionData.addAll(possibleSections);
        sectionsView.setItems(sectionData);
    }

    @FXML
    public void onAssign(ActionEvent actionEvent) {
        if (sectionsView.getSelectionModel().getSelectedItem() != null) {
            Section selected = sectionsView.getSelectionModel().getSelectedItem();
            instructor.getCourses().add(selected.getCourse());
            stage.close();
        }
    }

    @FXML
    public void onDelete(ActionEvent actionEvent) {
            Section selected = sectionsView.getSelectionModel().getSelectedItem();
            instructor.getCourses().remove(selected.getCourse());
            stage.close();
    }
}
