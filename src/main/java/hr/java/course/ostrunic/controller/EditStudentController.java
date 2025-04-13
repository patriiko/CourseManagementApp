package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.LoginApplication;
import hr.java.course.ostrunic.enums.UserType;
import hr.java.course.ostrunic.genericsi.DataChange;
import hr.java.course.ostrunic.genericsi.Validator;
import hr.java.course.ostrunic.model.Student;
import hr.java.course.ostrunic.threads.UpdateStudentThread;
import hr.java.course.ostrunic.utils.AppendingObjectOutputStream;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import hr.java.course.ostrunic.utils.FileUtils;
import hr.java.course.ostrunic.utils.PasswordEncryption;
import javafx.event.ActionEvent;
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

public class EditStudentController {
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
    @FXML
    private TextField jmbagTextField;

    private Student student;

    public void setStudent(Student student) {
        this.student = student;

        firstNameTextField.setText(student.getFirstName());
        lastNameTextField.setText(student.getLastName());
        usernameTextField.setText(student.getUsername());
        emailTextField.setText(student.getEmail());
        emailTextField.setEditable(false);
        jmbagTextField.setText(student.getJmbag());
        jmbagTextField.setEditable(false);
    }

    public void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/hr/java/course/ostrunic/studentsSearchScreen.fxml"));
            Scene scene = new Scene(root);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException("Error while loading studentsSearchScreen.fxml", e);
        }
    }

    public void saveStudent() {
        Student oldStudent = new Student(student.getId(), student.getFirstName(), student.getLastName(), student.getEmail(), student.getUsername(), student.getPassword(), student.getJmbag());

        String newUsername = usernameTextField.getText();
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

        Student newStudent = new Student(student.getId(), firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), usernameTextField.getText(), encryptedPassword, jmbagTextField.getText());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to change the student?");
        alert.setContentText("You are about to change the student. Are you sure you want to proceed?");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            //DatabaseUtils.updateStudent(newStudent);
            UpdateStudentThread updateStudent = new UpdateStudentThread(newStudent);
            executor.execute(updateStudent);
            executor.shutdown();

            FileUtils.updateUserCredentials(student.getUsername(), newUsername, encryptedPassword);

            student = newStudent;

            DataChange<Student, Student> dataChange = new DataChange<>(
                    "Student",
                    oldStudent,
                    newStudent,
                    Timestamp.from(Instant.now()),
                    UserType.valueOf(LoginApplication.getCurrentUserType())
            );

            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
            alertInfo.setTitle("Potvrda");
            alertInfo.setHeaderText("Potvrda promjene studenta");
            alertInfo.setContentText("Student je uspješno promijenjen.");
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
