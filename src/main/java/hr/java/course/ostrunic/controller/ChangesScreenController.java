package hr.java.course.ostrunic.controller;

import hr.java.course.ostrunic.genericsi.DataChange;
import hr.java.course.ostrunic.threads.LastChangeThread;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static hr.java.course.ostrunic.main.Main.logger;

public class ChangesScreenController {
    @FXML
    private TableView<DataChange> changesTableView;
    @FXML
    private TableColumn<DataChange, String> objectColumn;
    @FXML
    private TableColumn<DataChange, String> oldValueColumn;
    @FXML
    private TableColumn<DataChange, String> newValueColumn;
    @FXML
    private TableColumn<DataChange, String> dateTimeColumn;
    @FXML
    private TableColumn<DataChange, String> userTypeColumn;

    public void initialize() {

        objectColumn.setCellValueFactory(new PropertyValueFactory<>("objectName"));
        oldValueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOldValue().toString()));
        newValueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNewValue().toString()));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));

        ObservableList<DataChange> dataChanges = FXCollections.observableArrayList();

        try (FileInputStream fileIn = new FileInputStream("dat/promjene.bin");
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

            DataChange dataChange;
            while ((dataChange = (DataChange) objectIn.readObject()) != null) {
                dataChanges.add(dataChange);
            }

        } catch (EOFException e) {
            logger.info("End of file reached");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        changesTableView.setItems(dataChanges);

        LastChangeThread lastChangeThread = new LastChangeThread();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(lastChangeThread);
        executor.shutdown();
    }
}
