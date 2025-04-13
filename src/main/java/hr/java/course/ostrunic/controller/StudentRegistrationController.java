package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.enums.UserType;
import hr.java.course.ostrunic.exceptions.InvalidEmailException;
import hr.java.course.ostrunic.exceptions.InvalidPasswordException;
import hr.java.course.ostrunic.genericsi.Validator;
import hr.java.course.ostrunic.model.Student;
import hr.java.course.ostrunic.threads.AddStudentToAdminThread;
import hr.java.course.ostrunic.threads.SaveStudentThread;
import hr.java.course.ostrunic.threads.SaveSystemUserThread;
import hr.java.course.ostrunic.utils.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static hr.java.course.ostrunic.main.Main.logger;


public class StudentRegistrationController {
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField JMBAGTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private PasswordField confirmPasswordPasswordField;
    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        backButton.setOnAction(event -> {
            try {
                switchToLoginScreen(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void switchToLoginScreen(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hr/java/course/ostrunic/loginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void resetFields() {
        firstNameTextField.setText("");
        lastNameTextField.setText("");
        JMBAGTextField.setText("");
        usernameTextField.setText("");
        emailTextField.setText("");
        passwordPasswordField.setText("");
        confirmPasswordPasswordField.setText("");
    }

    @FXML
    private void register() {
        if (!validateFields()) {
            incompleteFieldsAlert();
        } else if (!passwordsMatch()) {
            passwordsDoNotMatchAlert();
        } else {
            try {
                String username = usernameTextField.getText();
                if (FileUtils.userExists(username)) {
                    usernameExistsAlert();
                    return;
                }

                String password = passwordPasswordField.getText();
                Validator<String> passwordValidator = new Validator<>();
                passwordValidator.validatePassword(password);

                Long id = DatabaseUtils.getNextUserId();

                String firstName = firstNameTextField.getText();
                String lastName = lastNameTextField.getText();
                String jmbag = JMBAGTextField.getText();
                String email = emailTextField.getText();
                String encryptedPassword = PasswordEncryption.encryptPassword(password);

                Validator<String> emailValidator = new Validator<>();
                emailValidator.validateEmail(email);

                ExecutorService executor = Executors.newSingleThreadExecutor();

                Student newStudent = new Student(id, firstName, lastName, email, username, encryptedPassword, UserType.STUDENT, jmbag);

                //DatabaseUtils.saveSystemUser(firstName, lastName, username, email, encryptedPassword, String.valueOf(UserType.STUDENT));
                //DatabaseUtils.saveStudent(jmbag, id);
                //DatabaseUtils.addStudentToAdministrator(1L, id);

                SaveSystemUserThread saveSystemUserThread = new SaveSystemUserThread(firstName, lastName, username, email, encryptedPassword, String.valueOf(UserType.STUDENT));
                executor.execute(saveSystemUserThread);

                SaveStudentThread saveStudentThread = new SaveStudentThread(jmbag, id);
                executor.execute(saveStudentThread);

                AddStudentToAdminThread addStudentToAdminThread = new AddStudentToAdminThread(1L, id);
                executor.execute(addStudentToAdminThread);

                FileUtils.saveUserCredentials(username, encryptedPassword);

                SerializationUtils.serializeObject(newStudent, "dat/students.ser");

                executor.shutdown();
                successfullyRegisteredAlert();
                resetFields();
            }
            catch (NoSuchAlgorithmException | InvalidEmailException | InvalidPasswordException | IOException e) {
                if (e instanceof InvalidEmailException) {
                    invalidEmailAlert();
                    logger.error("Invalid email format");
                } else if (e instanceof InvalidPasswordException) {
                    invalidPasswordAlert();
                    logger.error("Invalid password format");
                }
                e.printStackTrace();
            }
        }
    }

    private static void incompleteFieldsAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Incomplete fields");
        alert.setContentText("Please fill out all fields.");
        alert.showAndWait();
    }

    private static void successfullyRegisteredAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Registration successful");
        alert.setContentText("You have successfully registered as a student.");
        alert.showAndWait();
    }

    private static void invalidEmailAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Invalid email format");
        alert.setContentText("Please enter a valid email address.");
        alert.showAndWait();
    }

    private static void invalidPasswordAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Invalid password format");
        alert.setContentText("Password should contain min. 8 characters,\n1 uppercase letter and 1 number.");
        alert.showAndWait();
    }

    private static void usernameExistsAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Username already exists");
        alert.setContentText("Please choose a different username.");
        alert.showAndWait();
    }

    private boolean validateFields() {
        return !firstNameTextField.getText().isEmpty()
                && !lastNameTextField.getText().isEmpty()
                && !JMBAGTextField.getText().isEmpty()
                && !usernameTextField.getText().isEmpty()
                && !emailTextField.getText().isEmpty()
                && !passwordPasswordField.getText().isEmpty()
                && !confirmPasswordPasswordField.getText().isEmpty();
    }

    private boolean passwordsMatch() {
        return passwordPasswordField.getText().equals(confirmPasswordPasswordField.getText());
    }

    private static void passwordsDoNotMatchAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Passwords are not matching.");
        alert.setContentText("Please try again.");
        alert.showAndWait();
    }
}