package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.LoginApplication;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import hr.java.course.ostrunic.utils.Session;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class StudentHomeScreenController {
    @FXML
    private Label greetingLabel;

    public void initialize() {
        greetingLabel.setAlignment(Pos.CENTER);
        greetingLabel.setText("Welcome back, " + LoginApplication.getCurrentUserType().toLowerCase() + " " + DatabaseUtils.getUsernameById(Session.getInstance().getLoggedInUserId()) + "!");
    }
}
