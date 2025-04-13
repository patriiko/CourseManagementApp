package hr.java.course.ostrunic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static hr.java.course.ostrunic.main.Main.logger;

public class LoginApplication extends Application {

    public static Stage mainStage;
    private static String currentUserType;

    @Override
    public void start(Stage stage) throws IOException {
        LoginApplication.setUserAgentStylesheet(getClass().getResource("/styles/primer-dark.css").toExternalForm());
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("loginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("Login application");
        stage.setScene(scene);
        stage.show();
        logger.info("Application started");
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getStage() {
        return mainStage;
    }

    public static String getCurrentUserType() {
        return currentUserType;
    }

    public static void setCurrentUserType(String userType) {
        currentUserType = userType;
    }
}