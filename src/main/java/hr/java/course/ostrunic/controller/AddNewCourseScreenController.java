package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.model.Course;
import hr.java.course.ostrunic.model.Task;
import hr.java.course.ostrunic.threads.AddCourseTaskThread;
import hr.java.course.ostrunic.threads.AddCourseToAdminThread;
import hr.java.course.ostrunic.threads.SaveCourseThread;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import hr.java.course.ostrunic.utils.SerializationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddNewCourseScreenController {
    @FXML
    private TextField nameTextField;
    @FXML
    private ListView<Task> taskListView;

    public void initialize() {
        taskListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        List<Task> taskList = DatabaseUtils.getTasks();
        ObservableList<Task> observableTaskList = FXCollections.observableArrayList(taskList);
        taskListView.getItems().addAll(taskList);
    }

    public void saveNewCourse() {
        if (isAnyInputEmpty()) {
            showAlert("Please fill in all required fields.");
            return;
        }
        Long id = DatabaseUtils.getNextCourseId();

        String name = nameTextField.getText();

        ObservableList<Task> selectedTasks = taskListView.getSelectionModel().getSelectedItems();
        List<Task> tasks = List.copyOf(selectedTasks);

        Course newCourse = new Course(id, name, tasks);

        ExecutorService executorService = Executors.newSingleThreadExecutor();

       // DatabaseUtils.saveCourse(newCourse);
       // DatabaseUtils.addCourseToAdministrator(1L, id);

        SaveCourseThread saveCourseThread = new SaveCourseThread(newCourse);
        executorService.execute(saveCourseThread);

        AddCourseToAdminThread addCourseToAdminThread = new AddCourseToAdminThread(1L, id);
        executorService.execute(addCourseToAdminThread);

        for(Task task : tasks) {
            //DatabaseUtils.addCourseTask(id, task.getId());
            AddCourseTaskThread addCourseTaskThread = new AddCourseTaskThread(id, task.getId());
            executorService.execute(addCourseTaskThread);
        }

        executorService.shutdown();
        SerializationUtils.serializeObject(newCourse, "dat/courses.ser");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Course added successfully!");
        alert.setHeaderText("Course added successfully!");
        alert.setContentText("The course '" + name + "' was successfully saved!");
        alert.showAndWait();
    }

    private boolean isAnyInputEmpty() {
        return nameTextField.getText().trim().isEmpty() ||
                taskListView.getSelectionModel().getSelectedItems().isEmpty();
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation error");
        alert.setHeaderText("There was one or more errors");
        alert.setContentText(content);
        alert.showAndWait();
    }
}
