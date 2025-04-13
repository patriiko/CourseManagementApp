package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.LoginApplication;
import hr.java.course.ostrunic.model.Task;
import hr.java.course.ostrunic.threads.DeleteTaskThread;
import hr.java.course.ostrunic.threads.GetTasksThread;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static hr.java.course.ostrunic.main.Main.logger;

public class TasksSearchScreenController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField taskNameTextField;
    @FXML
    private TableView<Task> taskTableView;
    @FXML
    private TableColumn<Task, String> taskIdColumn;
    @FXML
    private TableColumn<Task, String> taskNameColumn;
    @FXML
    private TableColumn<Task, String> taskDescriptionColumn;
    @FXML
    private TableColumn<Task, Boolean> taskCompletedColumn;
    @FXML
    private ContextMenu taskContextMenu;

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

        taskTableView.setContextMenu(taskContextMenu);

        taskIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Task, String> taskStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(taskStringCellDataFeatures.getValue().getId().toString());
            }
        });
        taskNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Task, String> taskStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(taskStringCellDataFeatures.getValue().getName());
            }
        });
        taskDescriptionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Task, String> taskStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(taskStringCellDataFeatures.getValue().getDescription());
            }
        });
        taskCompletedColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Task, Boolean> taskBooleanCellDataFeatures) {
                return new SimpleBooleanProperty(taskBooleanCellDataFeatures.getValue().isCompleted());
            }
        });
    }

    public void taskSearch() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        //List<Task> taskList = DatabaseUtils.getTasks();
        GetTasksThread getTasksThread = new GetTasksThread();
        executorService.execute(getTasksThread);
        List<Task> taskList = getTasksThread.getTasks();

        String taskName = taskNameTextField.getText();

        List<Task> filteredTaskList = taskList.stream()
                .filter(task -> task.getName().contains(taskName))
                .collect(Collectors.toList());

        ObservableList<Task> observableTaskList = FXCollections.observableArrayList(filteredTaskList);
        taskTableView.setItems(observableTaskList);
        executorService.shutdown();
    }

    public void deleteTask() {
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete task");
        alert.setHeaderText("Are you sure you want to delete the task?");
        alert.setContentText(selectedTask.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            //DatabaseUtils.deleteTask(selectedTask.getId());
            DeleteTaskThread deleteTask = new DeleteTaskThread(selectedTask.getId());
            Future<?> future = executorService.submit(deleteTask);
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error while deleting student", e);
            }

            taskSearch();

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("Task deleted");
            successAlert.setHeaderText("The task '" + selectedTask.getName() + "' was successfully deleted!");
            successAlert.showAndWait();
            executorService.shutdown();
        }
    }

    public void editTask() {
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Task Selected");
            alert.setHeaderText("Please select a task to edit.");
            alert.showAndWait();
            logger.error("No task selected for editing");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/java/course/ostrunic/editTask.fxml"));
            Parent editTaskScreen = loader.load();

            EditTaskController controller = loader.getController();
            controller.setTask(selectedTask);

            rootPane.getChildren().setAll(editTaskScreen);
        } catch (IOException e) {
            logger.error("Error while loading editTask.fxml", e);
            e.printStackTrace();
        }
    }
}
