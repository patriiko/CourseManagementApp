package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.LoginApplication;
import hr.java.course.ostrunic.enums.UserType;
import hr.java.course.ostrunic.genericsi.DataChange;
import hr.java.course.ostrunic.model.Task;
import hr.java.course.ostrunic.threads.UpdateTaskThread;
import hr.java.course.ostrunic.utils.AppendingObjectOutputStream;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static hr.java.course.ostrunic.main.Main.logger;

public class EditTaskController {
    @FXML
    private TextField taskNameTextField;
    @FXML
    private TextField taskDescriptionTextField;

    private Task task;

    public void setTask(Task task) {
        this.task = task;

        taskNameTextField.setText(task.getName());
        taskDescriptionTextField.setText(task.getDescription());
    }

    public void saveTask() {
        Task oldTask = new Task.Builder(task.getId(), task.getName())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .build();

        Task updatedTask = new Task.Builder(task.getId(), taskNameTextField.getText())
                .description(taskDescriptionTextField.getText())
                .completed(task.isCompleted())
                .build();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda");
        alert.setHeaderText("Potvrda promjene zadatka");
        alert.setContentText("Jeste li sigurni da želite promijeniti zadatak?");

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            //DatabaseUtils.updateTask(updatedTask);
            UpdateTaskThread updateTask = new UpdateTaskThread(updatedTask);
            executorService.execute(updateTask);
            executorService.shutdown();

            task = updatedTask;

            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
            alertInfo.setTitle("Potvrda");
            alertInfo.setHeaderText("Potvrda promjene zadatka");
            alertInfo.setContentText("Zadatak je uspješno promijenjen.");
            alertInfo.showAndWait();

            DataChange<Task, Task> dataChange = new DataChange<>(
                    "Task",
                    oldTask,
                    updatedTask,
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
