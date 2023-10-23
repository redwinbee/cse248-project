package me.valacritty.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import me.valacritty.extensions.region.TemporalClickableAnchorPane;
import me.valacritty.models.Instructor;

public class SectionAssignmentController {
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
    }
}
