package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.model.Course;
import hr.java.course.ostrunic.model.Task;
import hr.java.course.ostrunic.threads.GetCurrentUsersCoursesThread;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import hr.java.course.ostrunic.utils.Session;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ViewMyCoursesScreenController {
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<Course> coursesTableView;
    @FXML
    private TableColumn<Course, String> courseIdColumn;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, List<String>> courseTasksColumn;

    public void initialize() {
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
    }

    public void courseSearch() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Long userId = Session.getInstance().getLoggedInUserId();

        List<Course> courseList = DatabaseUtils.getCurrentUsersCourses(userId);
        //GetCurrentUsersCoursesThread getCurrentUsersCourses = new GetCurrentUsersCoursesThread(userId);
        //executorService.execute(getCurrentUsersCourses);
        //List<Course> courseList = getCurrentUsersCourses.getCoursesThread();

        Map<Long, Course> courseMap = courseList.stream().collect(Collectors.toMap(Course::getId, course -> course));

        String courseName = searchTextField.getText();
        List<Course> filteredCourseList;

        if(courseName == null || courseName.isEmpty()) {
            filteredCourseList = new ArrayList<>(courseMap.values());
        }
        else {
            filteredCourseList = courseList.stream()
                    .filter(course -> course.getName().contains(courseName))
                    .collect(Collectors.toList());
        }

        ObservableList<Course> observableCourseList = FXCollections.observableArrayList(filteredCourseList);
        coursesTableView.setItems(observableCourseList);
    }

    public void completeCourse() {
        Course selectedCourse = coursesTableView.getSelectionModel().getSelectedItem();
        Long studentId = Session.getInstance().getLoggedInUserId();

        DatabaseUtils.markCourseAsCompleted(studentId, selectedCourse.getId());
        DatabaseUtils.addStudentCertificate(studentId, selectedCourse.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Course completed successfully!");
        alert.setHeaderText("You have successfully completed the course!");
        alert.setContentText("The course '" + selectedCourse.getName() + "' was successfully completed!\nYou have received a certificate for completing the course!");
        alert.showAndWait();

        coursesTableView.getItems().remove(selectedCourse);
    }
}
