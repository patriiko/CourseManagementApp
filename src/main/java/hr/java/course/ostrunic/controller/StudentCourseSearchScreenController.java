package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.exceptions.CourseNotFoundException;
import hr.java.course.ostrunic.exceptions.InvalidCourseEnrollmentException;
import hr.java.course.ostrunic.model.Course;
import hr.java.course.ostrunic.model.Task;
import hr.java.course.ostrunic.threads.GetCoursesThread;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import hr.java.course.ostrunic.utils.Session;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class StudentCourseSearchScreenController {
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<Course> courseTableView;
    @FXML
    private TableColumn<Course, String> courseIdColumn;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, List<String>> courseTasksColumn;
    @FXML
    private Button enrollButton;


    public void initialize() {
        courseTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        courseIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Course, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Course, String> courseStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(courseStringCellDataFeatures.getValue().getId().toString());
            }
        });

        courseNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Course, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Course, String> courseStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(courseStringCellDataFeatures.getValue().getName());
            }
        });

        courseTasksColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTasks().stream().map(Task::getName).toList()));

        enrollButton.setOnAction(event -> enrollInCourses());
    }

    private void enrollInCourses() {
        ObservableList<Course> selectedCourses = courseTableView.getSelectionModel().getSelectedItems();
        Long studentId = Session.getInstance().getLoggedInUserId();
        for (Course course : selectedCourses) {
            try {
                DatabaseUtils.enrollInCourse(studentId, course);
            } catch (InvalidCourseEnrollmentException | CourseNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Enrollment Error");
                alert.setHeaderText("Enrollment failed");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Enrollment");
        alert.setHeaderText("Enrollment successful");
        alert.setContentText("You have successfully enrolled in the selected courses.");
        alert.showAndWait();
    }

    public void courseSearch() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        //List<Course> courseList = DatabaseUtils.getCourses();

        GetCoursesThread getCoursesThread = new GetCoursesThread();
        executorService.execute(getCoursesThread);
        List<Course> courseList = getCoursesThread.getCourses();
        executorService.shutdown();

        String courseName = searchTextField.getText();
        List<Course> filteredCourseList;

        if(courseName == null || courseName.isEmpty()) {
            filteredCourseList = courseList;
        }
        else {
            filteredCourseList = courseList.stream()
                    .filter(course -> course.getName().contains(courseName))
                    .collect(Collectors.toList());
        }

        ObservableList<Course> observableCourseList = FXCollections.observableArrayList(filteredCourseList);
        courseTableView.setItems(observableCourseList);
    }
}
