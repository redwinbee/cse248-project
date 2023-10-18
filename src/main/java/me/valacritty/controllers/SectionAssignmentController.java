package me.valacritty.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import me.valacritty.extensions.region.TemporalClickableRegion;
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
    private TemporalClickableRegion region;
    public void setUserData(Instructor selected, TemporalClickableRegion region) {
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
