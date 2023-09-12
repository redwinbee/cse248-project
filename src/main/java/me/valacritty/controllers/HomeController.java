package me.valacritty.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import me.valacritty.Main;
import me.valacritty.models.Campus;
import me.valacritty.models.Instructor;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    private final ObservableList<Instructor> instructorData = FXCollections.observableArrayList();
    @FXML
    public TableView<Instructor> instructorView;
    @FXML
    public TableColumn<Instructor, String> idCol;
    @FXML
    public TableColumn<Instructor, String> nameCol;
    @FXML
    public TableColumn<Instructor, String> rankCol;
    @FXML
    public TableColumn<Instructor, Campus> homeCampusCol;
    @FXML
    public TableColumn<Instructor, HashSet<Campus>> prefCampusCol;
    @FXML
    public TableColumn<Instructor, Boolean> onlCertifiedCol;
    @FXML
    public TableColumn<Instructor, ArrayList<String>> coursesCol;
    @FXML
    public TableColumn<Instructor, Boolean> secondCourseCol;
    @FXML
    public TableColumn<Instructor, Boolean> thirdCourseCol;
    @FXML
    private TextField queryField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));
        homeCampusCol.setCellValueFactory(new PropertyValueFactory<>("homeCampus"));
        prefCampusCol.setCellValueFactory(new PropertyValueFactory<>("preferredCampuses"));
        onlCertifiedCol.setCellValueFactory(new PropertyValueFactory<>("canTeachOnline"));
        coursesCol.setCellValueFactory(new PropertyValueFactory<>("courses"));
        secondCourseCol.setCellValueFactory(new PropertyValueFactory<>("canTeachSecondCourse"));
        thirdCourseCol.setCellValueFactory(new PropertyValueFactory<>("canTeachThirdCourse"));

        setBooleanColumnCellFactory(onlCertifiedCol);
        setBooleanColumnCellFactory(secondCourseCol);
        setBooleanColumnCellFactory(thirdCourseCol);

        instructorView.setItems(instructorData);
    }

    @FXML
    public void onSearch() {
        if (!queryField.getText().isBlank()) {
            String query = queryField.getText().toLowerCase().trim();
            instructorData.clear(); // Clear the existing data
            Main.getInstructors().stream()
                    .filter(instructor -> instructor.getName().toLowerCase().contains(query) || instructor.getId().contains(query))
                    .forEach(instructorData::add); // Add filtered instructors to the list
        }
    }
    private void setBooleanColumnCellFactory(TableColumn<Instructor, Boolean> column) {
        column.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Instructor, Boolean> call(TableColumn<Instructor, Boolean> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setStyle(""); // Reset the cell's style
                        } else {
                            setText(item.toString());

                            if (item) {
                                setStyle("-fx-background-color: #e3ffe3;");
                            } else {
                                setStyle("-fx-background-color: #ffe6e6;");
                            }
                        }
                    }
                };
            }
        });
    }
}
