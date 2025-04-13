package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.LoginApplication;
import hr.java.course.ostrunic.model.Course;
import hr.java.course.ostrunic.model.Instructor;
import hr.java.course.ostrunic.model.Student;
import hr.java.course.ostrunic.sort.SortByFirstName;
import hr.java.course.ostrunic.sort.SortById;
import hr.java.course.ostrunic.sort.SortByLastName;
import hr.java.course.ostrunic.threads.DeleteStudentThread;
import hr.java.course.ostrunic.threads.GetStudentsThread;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import hr.java.course.ostrunic.utils.FileUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static hr.java.course.ostrunic.main.Main.logger;

public class StudentsSearchScreenController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<Student> studentsTableView;
    @FXML
    private TableColumn<Student, String> studentIdColumn;
    @FXML
    private TableColumn<Student, String> studentFirstNameColumn;
    @FXML
    private TableColumn<Student, String> studentLastNameColumn;
    @FXML
    private TableColumn<Student, String> studentEmailColumn;
    @FXML
    private TableColumn<Student, String> studentUsernameColumn;
    @FXML
    private TableColumn<Student, String> studentPasswordColumn;
    @FXML
    private TableColumn<Student, String> studentJMBAGColumn;
    @FXML
    private TableColumn<Student, List<String>> studentCoursesColumn;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private MenuItem deleteMenuItem;
    @FXML
    private MenuItem editMenuItem;
    @FXML
    private ComboBox<String> sortComboBox;

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

        studentsTableView.setContextMenu(contextMenu);
        sortComboBox.getItems().addAll("ID", "First Name", "Last Name");

        studentIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Student, String> studentStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(studentStringCellDataFeatures.getValue().getId().toString());
            }
        });

        studentFirstNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Student, String> studentStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(studentStringCellDataFeatures.getValue().getFirstName());
            }
        });

        studentLastNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Student, String> studentStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(studentStringCellDataFeatures.getValue().getLastName());
            }
        });

        studentEmailColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Student, String> studentStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(studentStringCellDataFeatures.getValue().getEmail());
            }
        });

        studentUsernameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Student, String> studentStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(studentStringCellDataFeatures.getValue().getUsername());
            }
        });

        studentPasswordColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Student, String> studentStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(studentStringCellDataFeatures.getValue().getPassword());
            }
        });

        studentJMBAGColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Student, String> studentStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(studentStringCellDataFeatures.getValue().getJmbag());
            }
        });

        studentCoursesColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCourses().stream().map(Course::getName).toList()));
        sortComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            switch (newValue) {
                case "First Name" -> sortStudentsByFirstName();
                case "Last Name" -> sortStudentsByLastName();
                case "ID" -> sortStudentsById();
            }
        });
    }

    public void studentSearch() {
        //List<Student> studentList = DatabaseUtils.getStudents();
        GetStudentsThread getStudentsThread = new GetStudentsThread();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getStudentsThread);
        List<Student> studentList = getStudentsThread.getStudents();

        String studentName = searchTextField.getText();
        List<Student> filteredStudentList;

        if (studentName == null || studentName.isEmpty()) {
            filteredStudentList = studentList;
        } else {
            filteredStudentList = studentList.stream()
                    .filter(student -> student.getFirstName().contains(studentName))
                    .collect(Collectors.toList());
        }

        ObservableList<Student> observableStudentList = FXCollections.observableArrayList(filteredStudentList);
        studentsTableView.setItems(observableStudentList);
        executor.shutdown();
    }

    public void deleteStudent() {
        Student selectedStudent = studentsTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Student");
        alert.setContentText("Are you sure you want to delete this student?");

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            //DatabaseUtils.deleteStudent(selectedStudent.getId());
            DeleteStudentThread deleteStudentThread = new DeleteStudentThread(selectedStudent.getId());
            Future<?> future = executorService.submit(deleteStudentThread);
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error while deleting student", e);
            }

            FileUtils.deleteUserCredentials(selectedStudent.getUsername(), selectedStudent.getPassword());
            studentSearch();
            executorService.shutdown();

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("Student deleted");
            successAlert.setContentText("Student has been successfully deleted");
            successAlert.showAndWait();
        }
    }

    public void editStudent() {
        Student selectedStudent = studentsTableView.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("No Instructor Selected");
            alert.setContentText("Please select an instructor to edit!");
            alert.showAndWait();
            logger.error("No instructor selected for editing");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/java/course/ostrunic/editStudent.fxml"));
            Parent root = loader.load();

            EditStudentController controller = loader.getController();
            controller.setStudent(selectedStudent);

            Stage stage = (Stage) studentsTableView.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            logger.error("Error while loading editInstructor.fxml", e);
        }
    }

    public void sortStudentsByFirstName() {
        SortByFirstName<Student> sortByFirstName = new SortByFirstName<>();
        ObservableList<Student> studentList = studentsTableView.getItems();
        Collections.sort(studentList, sortByFirstName);
        studentsTableView.setItems(studentList);
    }

    public void sortStudentsByLastName() {
        SortByLastName<Student> sortByLastName = new SortByLastName<>();
        ObservableList<Student> studentList = studentsTableView.getItems();
        Collections.sort(studentList, sortByLastName);
        studentsTableView.setItems(studentList);
    }

    public void sortStudentsById() {
        SortById<Student> sortById = new SortById<>();
        ObservableList<Student> studentList = studentsTableView.getItems();
        Collections.sort(studentList, sortById);
        studentsTableView.setItems(studentList);
    }
}
