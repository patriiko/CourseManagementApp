package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.LoginApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class InstructorMenuBarController {
    public void showLoginScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/loginScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStudentsSearchScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/studentsSearchScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTasksSearchScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/tasksSearchScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddNewTaskScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/addNewTaskScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCoursesSearchScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/coursesSearchScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCertificatesSearchScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/certificatesSearchScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
