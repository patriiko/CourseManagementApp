package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.LoginApplication;
import hr.java.course.ostrunic.exceptions.AlreadyTeachingCourseException;
import hr.java.course.ostrunic.exceptions.CourseNotFoundException;
import hr.java.course.ostrunic.model.Course;
import hr.java.course.ostrunic.model.Task;
import hr.java.course.ostrunic.threads.BeginLecturingThread;
import hr.java.course.ostrunic.threads.DeleteCourseThread;
import hr.java.course.ostrunic.threads.GetCoursesThread;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import hr.java.course.ostrunic.utils.Session;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import hr.java.course.ostrunic.exceptions.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static hr.java.course.ostrunic.main.Main.logger;

public class CoursesSearchScreenController {
    @FXML
    private AnchorPane rootPane;
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
    @FXML
    private ContextMenu contextMenu;

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

        coursesTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        coursesTableView.setContextMenu(contextMenu);

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

        courseTasksColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTasks().stream().map(Task::toString).toList()));
    }

    public void courseSearch() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        //List<Course> courseList = DatabaseUtils.getCourses();

        GetCoursesThread getCoursesThread = new GetCoursesThread();
        executorService.execute(getCoursesThread);
        List<Course> courseList = getCoursesThread.getCourses();

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
        coursesTableView.setItems(observableCourseList);
        executorService.shutdown();
    }

    public void beginLecturing() throws AlreadyTeachingCourseException, CourseNotFoundException {
        ObservableList<Course> selectedCourses = coursesTableView.getSelectionModel().getSelectedItems();
        Long instructorId = Session.getInstance().getLoggedInUserId();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        for (Course course : selectedCourses) {
            DatabaseUtils.beginLecturing(instructorId, course);
            //BeginLecturingThread beginLecturing = new BeginLecturingThread(instructorId, course);
            //executorService.execute(beginLecturing);
        }

        executorService.shutdown();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Course lecturing");
        alert.setHeaderText("Course lecturing started successfully!");
        alert.setContentText("You have successfully started lecturing the selected courses.");
        alert.showAndWait();
    }

    public void deleteCourse() {
        Course selectedCourse = coursesTableView.getSelectionModel().getSelectedItem();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Course");
        alert.setContentText("Are you sure you want to delete this course?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            //DatabaseUtils.deleteCourse(selectedCourse.getId());
            DeleteCourseThread deleteCourse = new DeleteCourseThread(selectedCourse.getId());
            Future<?> future = executorService.submit(deleteCourse);
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error while deleting student", e);
            }

            courseSearch();

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Course deletion");
            successAlert.setHeaderText("Course deleted successfully!");
            successAlert.setContentText("The course '" + selectedCourse.getName() + "' was successfully deleted!");
            successAlert.showAndWait();
            executorService.shutdown();
        }
    }

    public void editCourse() {
        Course selectedCourse = coursesTableView.getSelectionModel().getSelectedItem();

        if(selectedCourse == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Edit course");
            alert.setHeaderText("No course selected");
            alert.setContentText("Please select a course to edit.");
            alert.showAndWait();
            logger.error("No course selected for editing");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/java/course/ostrunic/editCourse.fxml"));
            AnchorPane editCourseScreen = loader.load();

            EditCourseController controller = loader.getController();
            controller.setCourse(selectedCourse);

            rootPane.getChildren().setAll(editCourseScreen);
        } catch (IOException e) {
            logger.error("Error while loading editCourse.fxml", e);
            e.printStackTrace();
        }
    }
}
