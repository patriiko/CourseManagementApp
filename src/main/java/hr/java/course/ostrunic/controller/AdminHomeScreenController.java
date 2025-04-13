package hr.java.course.ostrunic.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class AdminHomeScreenController {
    @FXML
    private Label greetingLabel;

    @FXML
    public void initialize() {
        greetingLabel.setText("Welcome back, Admin!");
        greetingLabel.setAlignment(Pos.CENTER);
    }
}
