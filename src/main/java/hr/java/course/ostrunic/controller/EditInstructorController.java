package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.LoginApplication;
import hr.java.course.ostrunic.enums.UserType;
import hr.java.course.ostrunic.genericsi.DataChange;
import hr.java.course.ostrunic.genericsi.Validator;
import hr.java.course.ostrunic.model.Instructor;
import hr.java.course.ostrunic.threads.UpdateInstructorThread;
import hr.java.course.ostrunic.utils.AppendingObjectOutputStream;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import hr.java.course.ostrunic.utils.FileUtils;
import hr.java.course.ostrunic.utils.PasswordEncryption;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static hr.java.course.ostrunic.main.Main.logger;

public class EditInstructorController {
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private PasswordField confirmPasswordPasswordField;

    private Instructor instructor;

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;

        firstNameTextField.setText(instructor.getFirstName());
        lastNameTextField.setText(instructor.getLastName());
        usernameTextField.setText(instructor.getUsername());
        emailTextField.setText(instructor.getEmail());
        emailTextField.setEditable(false);
    }

    public void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/hr/java/course/ostrunic/instructorsSearchScreen.fxml"));
            Scene scene = new Scene(root);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException("Error while loading InstructorsSearchScreen.fxml", e);
        }
    }

    public void saveInstructor() {
        Instructor oldInstructor = new Instructor(instructor.getId(), instructor.getFirstName(), instructor.getLastName(), instructor.getEmail(), instructor.getUsername(), instructor.getPassword());


        if (!passwordPasswordField.getText().equals(confirmPasswordPasswordField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Passwords Do Not Match");
            alert.setContentText("Please make sure the passwords match!");
            alert.showAndWait();
            return;
        }

        String password = passwordPasswordField.getText();
        Validator<String> passwordValidator = new Validator<>();
        if (!passwordValidator.isValidPasswordFormat(password)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Invalid Password");
            alert.setContentText("The password does not meet the required criteria!");
            alert.showAndWait();
            return;
        }

        String encryptedPassword = null;
        try {
            encryptedPassword = PasswordEncryption.encryptPassword(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        String newUsername = usernameTextField.getText();
        if (!instructor.getUsername().equals(newUsername) && FileUtils.isUsernameTaken(newUsername)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Username Taken");
            alert.setContentText("The username is already taken. Please choose a different username.");
            alert.showAndWait();
            return;
        }

        Instructor newInstructor = new Instructor(instructor.getId(), firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), usernameTextField.getText(), encryptedPassword);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to change the instructor?");
        alert.setContentText("You are about to change the instructor. Are you sure you want to proceed?");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            //DatabaseUtils.updateInstructor(newInstructor);
            UpdateInstructorThread updateInstructor = new UpdateInstructorThread(newInstructor);
            executor.execute(updateInstructor);
            executor.shutdown();

            FileUtils.updateUserCredentials(instructor.getUsername(), newUsername, encryptedPassword);

            instructor = newInstructor;

            DataChange<Instructor, Instructor> dataChange = new DataChange<>(
                    "Instructor",
                    oldInstructor,
                    newInstructor,
                    Timestamp.from(Instant.now()),
                    UserType.valueOf(LoginApplication.getCurrentUserType())
            );

            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
            alertInfo.setTitle("Confirmation Dialog");
            alertInfo.setHeaderText("Instructor Changed");
            alertInfo.setContentText("The instructor was successfully changed.");
            alertInfo.showAndWait();

            try (FileOutputStream fileOut = new FileOutputStream("dat/promjene.bin", true);
                 ObjectOutputStream objectOut = new AppendingObjectOutputStream(fileOut)) {
                objectOut.writeObject(dataChange);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Error while writing changes to file.");
            }
        }
    }
}
