package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.model.Certificate;
import hr.java.course.ostrunic.threads.GetCertificatesForStudentThread;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import hr.java.course.ostrunic.utils.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewMyCertificatesScreenController {
    @FXML
    TableView<Certificate> certificatesTableView;
    @FXML
    TableColumn<Certificate, String> courseNameColumn;
    @FXML
    TableColumn<Certificate, String> certificateNameColumn;

    public void initialize() {
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        certificateNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    public void showCertificates() {
        Long studentId = Session.getInstance().getLoggedInUserId();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        //Set<Certificate> certificates = DatabaseUtils.getCertificatesForStudent(studentId);

        GetCertificatesForStudentThread getCertificatesForStudent = new GetCertificatesForStudentThread(studentId);
        executorService.execute(getCertificatesForStudent);
        Set<Certificate> certificates = getCertificatesForStudent.getCertificatesForStudentThread(studentId);
        executorService.shutdown();

        ObservableList<Certificate> observableList = FXCollections.observableArrayList(certificates);
        certificatesTableView.setItems(observableList);
    }
}
