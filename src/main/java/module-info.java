module hr.java.course.ostrunic {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;

    opens hr.java.course.ostrunic to javafx.fxml;
    opens hr.java.course.ostrunic.controller to javafx.fxml;
    opens hr.java.course.ostrunic.genericsi to javafx.base;

    exports hr.java.course.ostrunic;
    exports hr.java.course.ostrunic.controller;
    exports hr.java.course.ostrunic.model;
    exports hr.java.course.ostrunic.abstracts;
    exports hr.java.course.ostrunic.records;
    exports hr.java.course.ostrunic.threads;
    exports hr.java.course.ostrunic.exceptions;
}