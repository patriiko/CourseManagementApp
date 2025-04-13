package hr.java.course.ostrunic.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AuthenticateInstructorController {
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Button authenticateButton;

    @FXML
    private void initialize() {}

    @FXML
    private void authenticate(ActionEvent event) throws IOException {
        if (!validateFields()) {
            emptyPasswordFieldAlert();
        } else {
            String enteredPassword = passwordPasswordField.getText();
            String storedPassword = Files.readString(Paths.get("dat/authenticationInstructorPassword.txt")).trim();

            if (enteredPassword.equals(storedPassword)) {
                switchToInstructorRegistrationScreen(event);
            } else {
                wrongAuthenticationPasswordAlert();
            }
        }
    }

    private static void wrongAuthenticationPasswordAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Wrong authentication password.");
        alert.setContentText("Please try again or contact the administrator.");
        alert.showAndWait();
    }

    private static void emptyPasswordFieldAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Empty field.");
        alert.setContentText("Please enter the authentication password.");
        alert.showAndWait();
    }

    private boolean validateFields() {
        return !passwordPasswordField.getText().isEmpty();
    }

    private void switchToInstructorRegistrationScreen(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hr/java/course/ostrunic/instructorRegistrationScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
