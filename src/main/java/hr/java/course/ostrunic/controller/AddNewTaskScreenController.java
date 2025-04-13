package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.LoginApplication;
import hr.java.course.ostrunic.model.Task;
import hr.java.course.ostrunic.threads.GetTasksThread;
import hr.java.course.ostrunic.threads.SaveTaskThread;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import hr.java.course.ostrunic.utils.SerializationUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddNewTaskScreenController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField taskNameTextField;
    @FXML
    private TextField taskDescriptionTextField;

    public void initialize() {
        String menuBarFXML;
        if ("ADMINISTRATOR".equals(LoginApplication.getCurrentUserType())) {
            menuBarFXML = "/hr/java/course/ostrunic/adminMenuBar.fxml";
        } else if ("INSTRUCTOR".equals(LoginApplication.getCurrentUserType())) {
            menuBarFXML = "/hr/java/course/ostrunic/instructorMenuBar.fxml";
        } else {
            throw new IllegalStateException("Unknown user type");
        }

        try {
            Node menuBar = FXMLLoader.load(getClass().getResource(menuBarFXML));
            rootPane.getChildren().add(menuBar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveNewTask() {
        Long id = DatabaseUtils.getNextTaskId();

        String name = taskNameTextField.getText();
        String description = taskDescriptionTextField.getText();

        List<String> errorMessages = validateInput(name, description);

        if (!errorMessages.isEmpty()) {
            showAlert(String.join("\n", errorMessages));
            return;
        }

        Task newTask = new Task.Builder(id, name)
                .description(description)
                .completed(false)
                .build();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        //List<Task> tasks = DatabaseUtils.getTasks();
        GetTasksThread getTasks = new GetTasksThread();
        executorService.execute(getTasks);
        List<Task> tasks = getTasks.getTasksThread();

        tasks.add(newTask);

        //DatabaseUtils.saveTask(newTask);
        SaveTaskThread saveTask = new SaveTaskThread(newTask);
        executorService.execute(saveTask);
        executorService.shutdown();

        SerializationUtils.serializeObject(newTask, "dat/tasks.ser");

        resetFields();
        successfullyCreatedTask(name);
    }

    private static void successfullyCreatedTask(String name) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task added successfully!");
        alert.setHeaderText("Task added successfully!");
        alert.setContentText("The task '" + name + "' was successfully saved!");
        alert.showAndWait();
    }

    private List<String> validateInput(String name, String description) {
        List<String> errorMessages = new ArrayList<>();

        if (name.isEmpty()) {
            errorMessages.add("Task name must not be empty!");
        }

        if (description.isEmpty()) {
            errorMessages.add("Task description must not be empty!");
        }

        return errorMessages;
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation error");
        alert.setHeaderText("There was one or more errors");
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void resetFields() {
        taskNameTextField.clear();
        taskDescriptionTextField.clear();
    }
}
