package hr.java.course.ostrunic.threads;

import hr.java.course.ostrunic.genericsi.DataChange;
import hr.java.course.ostrunic.interfaces.LastChangeDialog;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Optional;

public class LastChangeThread implements Runnable, LastChangeDialog {
    @Override
    public void run() {
        while (true) {
            Optional<DataChange> lastChangeOptional = readLastChange();
            DataChange lastChange = lastChangeOptional.get();
            Platform.runLater(() -> {
                showLastChangeDialog(lastChange);
            });

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Optional<DataChange> readLastChange() {
        DataChange lastChange = null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("dat/promjene.bin"))) {
            while (true) {
                try {
                    lastChange = (DataChange) in.readObject();
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(lastChange);
    }

    @Override
    public void showLastChangeDialog(DataChange lastChange) {
        if (lastChange != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Zadnja promjena");
            alert.setHeaderText("Zadnja promjena podataka");
            alert.setContentText("Zadnje promijenjeni podatak je " + lastChange.getObjectName() +
                    "\nStara vrijednost: " + lastChange.getOldValue() +
                    "\nNova vrijednost: " + lastChange.getNewValue() +
                    "\nDatum i vrijeme: " + lastChange.getTimestamp() +
                    "\nRole: " + lastChange.getUserType());

            alert.getDialogPane().setMinWidth(600);
            alert.getDialogPane().setMinHeight(300);

            alert.showAndWait();
        }
    }
}
