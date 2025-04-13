package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.LoginApplication;
import hr.java.course.ostrunic.enums.UserType;
import hr.java.course.ostrunic.genericsi.DataChange;
import hr.java.course.ostrunic.model.Course;
import hr.java.course.ostrunic.model.Task;
import hr.java.course.ostrunic.threads.UpdateCourseThread;
import hr.java.course.ostrunic.utils.AppendingObjectOutputStream;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static hr.java.course.ostrunic.main.Main.logger;

public class EditCourseController {
    @FXML
    private TextField courseNameTextField;

    private Course course;

    public void setCourse(Course course) {
        this.course = course;

        courseNameTextField.setText(course.getName());
    }

    public void saveCourse() {
        Course oldCourse = new Course(course.getId(), course.getName());

        Course updatedCourse = new Course(course.getId(), courseNameTextField.getText());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda");
        alert.setHeaderText("Potvrda promjene tečaja");
        alert.setContentText("Jeste li sigurni da želite promijeniti tečaj?");

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            //DatabaseUtils.updateCourse(updatedCourse);
            UpdateCourseThread updateCourse = new UpdateCourseThread(updatedCourse);
            executorService.execute(updateCourse);
            executorService.shutdown();

            course = updatedCourse;

            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Potvrda");
            alert1.setHeaderText("Potvrda promjene tečaja");
            alert1.setContentText("Tečaj je uspješno promijenjen.");
            alert1.showAndWait();

            DataChange<Course, Course> dataChange = new DataChange<>(
                    "Course",
                    oldCourse,
                    updatedCourse,
                    Timestamp.from(Instant.now()),
                    UserType.valueOf(LoginApplication.getCurrentUserType())
            );

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
