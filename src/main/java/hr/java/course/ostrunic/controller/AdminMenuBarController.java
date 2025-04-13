package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.LoginApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static hr.java.course.ostrunic.main.Main.logger;

public class AdminMenuBarController {
    public void showLoginScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/loginScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error while loading login screen. " + e.getMessage());
        }
    }

    public void showStudentsSearchScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/studentsSearchScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error while loading students search screen. " + e.getMessage());
        }
    }

    public void showInstructorsSearchScreen() {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/instructorsSearchScreen.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 800, 600);
                LoginApplication.getStage().setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Error while loading instructors search screen. " + e.getMessage());
            }
    }

    public void showTasksSearchScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/tasksSearchScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error while loading tasks search screen. " + e.getMessage());
        }
    }

    public void showAddNewTaskScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/addNewTaskScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error while loading add new task screen. " + e.getMessage());
        }
    }

    public void showCoursesSearchScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/coursesSearchScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error while loading courses search screen. " + e.getMessage());
        }
    }

    public void showAddNewCourseScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/addNewCourseScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error while loading add new course screen. " + e.getMessage());
        }
    }

    public void showCertificatesSearchScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/certificatesSearchScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            LoginApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error while loading certificates search screen. " + e.getMessage());
        }
    }

    public void showChangesScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/hr/java/course/ostrunic/changesScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 660);
            Stage stage = LoginApplication.getStage();
            stage.setScene(scene);
            stage.setTitle("Changes");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error while loading changes screen. " + e.getMessage());
        }
    }

}
