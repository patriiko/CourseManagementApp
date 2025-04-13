package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.LoginApplication;
import hr.java.course.ostrunic.model.Certificate;
import hr.java.course.ostrunic.model.Course;
import hr.java.course.ostrunic.threads.GetCertificatesThread;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CertificatesSearchScreenController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField courseNameTextField;
    @FXML
    private TableView<Certificate> certificatesTableView;
    @FXML
    private TableColumn<Certificate, String> certificateIdColumn;
    @FXML
    private TableColumn<Certificate, String> certificateDescriptionColumn;
    @FXML
    private TableColumn<Certificate, String> courseCertificateColumn;

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

        certificateIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Certificate, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Certificate, String> certificateStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(certificateStringCellDataFeatures.getValue().getId().toString());
            }
        });

        certificateDescriptionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Certificate, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Certificate, String> certificateStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(certificateStringCellDataFeatures.getValue().getDescription());
            }
        });

        courseCertificateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Certificate, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Certificate, String> certificateStringCellDataFeatures) {
                Course course = certificateStringCellDataFeatures.getValue().getCourse();
                String courseName = course != null ? course.getName() : "N/A";
                return new ReadOnlyStringWrapper(courseName);
            }
        });
    }

    public void searchCertificates() {
        String courseName = courseNameTextField.getText();
        //List<Certificate> certificates = DatabaseUtils.getCertificates();
        List<Certificate> filteredCertificatesList;

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        GetCertificatesThread getCertificatesThread = new GetCertificatesThread();
        executorService.execute(getCertificatesThread);
        List<Certificate> certificates = getCertificatesThread.getCertificates();
        executorService.shutdown();

        if (courseName == null || courseName.isEmpty()) {
            filteredCertificatesList = certificates;
        }
        else {
            filteredCertificatesList = certificates.stream()
                    .filter(certificate -> certificate.getCourse() != null && courseName.equals(certificate.getCourse().getName()))
                    .collect(Collectors.toList());
        }

        ObservableList<Certificate> certificateObservableList = FXCollections.observableArrayList(filteredCertificatesList);
        certificatesTableView.setItems(certificateObservableList);
    }
}
