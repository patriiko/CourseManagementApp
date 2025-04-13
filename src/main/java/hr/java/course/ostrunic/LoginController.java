package hr.java.course.ostrunic;

import hr.java.course.ostrunic.utils.DatabaseUtils;
import hr.java.course.ostrunic.utils.FileUtils;
import hr.java.course.ostrunic.utils.PasswordEncryption;
import hr.java.course.ostrunic.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import static hr.java.course.ostrunic.main.Main.logger;

public class LoginController {
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink studentRegistrationHyperlink;
    @FXML
    private Hyperlink instructorRegistrationHyperlink;

    public void initialize() {
        //loginButton.getStyleClass().add("login-button");
        studentRegistrationHyperlink.setOnAction(event -> {
            try {
                switchToScreen("studentRegistrationScreen.fxml", event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        instructorRegistrationHyperlink.setOnAction(event -> {
            try {
                switchToScreen("authenticateInstructorScreen.fxml", event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        loginButton.setOnAction(event -> {
            try {
                login(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @FXML
    private void login(ActionEvent event) throws Exception {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();
        String hashedPassword = PasswordEncryption.encryptPassword(password);

        Long userId = DatabaseUtils.getUserId(username);
        Session.getInstance().setLoggedInUserId(userId);
        System.out.println("Logged in user ID: " + userId);

        if (username.isEmpty() || password.isEmpty()) {
            emptyFieldAlert();
        } else if (FileUtils.findUserCredentials(username, hashedPassword)) {
            String userType = DatabaseUtils.getUserType(username);
            LoginApplication.setCurrentUserType(userType);
            switch (userType) {
                case "ADMINISTRATOR":
                    switchToScreen("adminHomeScreen.fxml", event);
                    break;
                case "INSTRUCTOR":
                    switchToScreen("instructorHomeScreen.fxml", event);
                    break;
                case "STUDENT":
                    switchToScreen("studentHomeScreen.fxml", event);
                    break;
                default:
                    invalidLoginAlert();
                    break;
            }
        } else {
            invalidLoginAlert();
            logger.error("Invalid login alert");
        }
    }

    private static void emptyFieldAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Empty field");
        alert.setContentText("Please enter both username and password.");
        alert.showAndWait();
    }

    private static void invalidLoginAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid login");
        alert.setContentText("The username or password is incorrect.");
        alert.showAndWait();
    }

    private void switchToScreen(String screenPath, ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(screenPath));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
