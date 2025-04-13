package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.LoginApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class StudentMenuBarController {
    public void showLoginScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/loginScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStudentCourseSearchScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/studentCourseSearchScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showViewMyCoursesScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/ViewMyCoursesScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showViewMyCertificatesScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/ViewMyCertificatesScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
