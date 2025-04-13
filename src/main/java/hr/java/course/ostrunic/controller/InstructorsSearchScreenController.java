package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.model.*;
import hr.java.course.ostrunic.sort.SortByFirstName;
import hr.java.course.ostrunic.sort.SortById;
import hr.java.course.ostrunic.sort.SortByLastName;
import hr.java.course.ostrunic.threads.DeleteInstructorThread;
import hr.java.course.ostrunic.threads.GetInstructorsThread;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import hr.java.course.ostrunic.utils.FileUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static hr.java.course.ostrunic.main.Main.logger;

public class InstructorsSearchScreenController {
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<Instructor> instructorsTableView;
    @FXML
    private TableColumn<Instructor, String> instructorIdColumn;
    @FXML
    private TableColumn<Instructor, String> instructorFirstNameColumn;
    @FXML
    private TableColumn<Instructor, String> instructorLastNameColumn;
    @FXML
    private TableColumn<Instructor, String> instructorEmailColumn;
    @FXML
    private TableColumn<Instructor, String> instructorUsernameColumn;
    @FXML
    private TableColumn<Instructor, String> instructorPasswordColumn;
    @FXML
    private TableColumn<Instructor, List<String>> instructorCoursesTaughtColumn;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private ComboBox<String> sortComboBox;

    public void initialize() {
        instructorsTableView.setContextMenu(contextMenu);
        sortComboBox.getItems().addAll("ID", "First Name", "Last Name");

        instructorIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instructor, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Instructor, String> instructorStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(instructorStringCellDataFeatures.getValue().getId().toString());
            }
        });
        instructorFirstNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instructor, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Instructor, String> instructorStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(instructorStringCellDataFeatures.getValue().getFirstName());
            }
        });
        instructorLastNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instructor, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Instructor, String> instructorStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(instructorStringCellDataFeatures.getValue().getLastName());
            }
        });
        instructorEmailColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instructor, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Instructor, String> instructorStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(instructorStringCellDataFeatures.getValue().getEmail());
            }
        });
        instructorUsernameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instructor, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Instructor, String> instructorStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(instructorStringCellDataFeatures.getValue().getUsername());
            }
        });
        instructorPasswordColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instructor, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Instructor, String> instructorStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(instructorStringCellDataFeatures.getValue().getPassword());
            }
        });
        instructorCoursesTaughtColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCoursesTaught().stream().map(Course::getName).toList()));

        sortComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            switch (newValue) {
                case "First Name" -> sortInstructorsByFirstName();
                case "Last Name" -> sortInstructorsByLastName();
                case "ID" -> sortInstructorsById();
            }
        });
    }

    public void instructorSearch() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        //List<Instructor> instructorList = DatabaseUtils.getInstructors();

        GetInstructorsThread getInstructorsThread = new GetInstructorsThread();
        executor.execute(getInstructorsThread);
        List<Instructor> instructorList = getInstructorsThread.getInstructors();

        String instructorName = searchTextField.getText();
        List<Instructor> filteredInstructorList;

        if (instructorName == null || instructorName.isEmpty()) {
            filteredInstructorList = instructorList;
        } else {
            filteredInstructorList = instructorList.stream()
                    .filter(student -> student.getFirstName().contains(instructorName))
                    .collect(Collectors.toList());
        }

        ObservableList<Instructor> observableInstructorList = FXCollections.observableArrayList(filteredInstructorList);
        instructorsTableView.setItems(observableInstructorList);
        executor.shutdown();
    }

    public void deleteInstructor() {
        Instructor selectedInstructor = instructorsTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Instructor");
        alert.setContentText("Are you sure you want to delete this instructor?");

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            //DatabaseUtils.deleteInstructor(selectedInstructor.getId());
            DeleteInstructorThread deleteInstructorThread = new DeleteInstructorThread(selectedInstructor.getId());
            Future<?> future = executorService.submit(deleteInstructorThread);
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error while deleting student", e);
            }

            FileUtils.deleteUserCredentials(selectedInstructor.getUsername(), selectedInstructor.getPassword());
            instructorSearch();
            executorService.shutdown();

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Information Dialog");
            successAlert.setHeaderText("Instructor Deleted");
            successAlert.setContentText("The instructor has been deleted successfully!");
            successAlert.showAndWait();
        }
    }

    public void editInstructor() {
        Instructor selectedInstructor = instructorsTableView.getSelectionModel().getSelectedItem();
        if (selectedInstructor == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("No Instructor Selected");
            alert.setContentText("Please select an instructor to edit!");
            alert.showAndWait();
            logger.error("No instructor selected for editing");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/java/course/ostrunic/editInstructor.fxml"));
            Parent root = loader.load();

            EditInstructorController controller = loader.getController();
            controller.setInstructor(selectedInstructor);

            Stage stage = (Stage) instructorsTableView.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            logger.error("Error while loading editInstructor.fxml", e);
        }
    }

    public void sortInstructorsByFirstName() {
        SortByFirstName<Instructor> sortByFirstName = new SortByFirstName<>();
        ObservableList<Instructor> instructorList = instructorsTableView.getItems();
        Collections.sort(instructorList, sortByFirstName);
        instructorsTableView.setItems(instructorList);
    }

    public void sortInstructorsByLastName() {
        SortByLastName<Instructor> sortByLastName = new SortByLastName<>();
        ObservableList<Instructor> instructorList = instructorsTableView.getItems();
        Collections.sort(instructorList, sortByLastName);
        instructorsTableView.setItems(instructorList);
    }

    public void sortInstructorsById() {
        SortById<Instructor> sortById = new SortById<>();
        ObservableList<Instructor> instructorList = instructorsTableView.getItems();
        Collections.sort(instructorList, sortById);
        instructorsTableView.setItems(instructorList);
    }
}
