package me.valacritty.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import me.valacritty.Main;
import me.valacritty.models.Campus;
import me.valacritty.models.Instructor;

import java.net.URL;
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
    private TextField queryField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));
        homeCampusCol.setCellValueFactory(new PropertyValueFactory<>("homeCampus"));
        prefCampusCol.setCellValueFactory(new PropertyValueFactory<>("preferredCampuses"));
        instructorView.setItems(instructorData);
    }

    @FXML
    public void onSearch() {
        if (!queryField.getText().isBlank()) {
            String query = queryField.getText();
            instructorData.clear(); // Clear the existing data
            Main.getInstructors().stream()
                    .filter(instructor -> instructor.toString().contains(query))
                    .forEach(instructorData::add); // Add filtered instructors to the list
        }
    }
}
