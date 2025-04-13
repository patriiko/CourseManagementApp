package hr.java.course.ostrunic.interfaces;

import hr.java.course.ostrunic.model.Student;

public sealed interface StudentInterface permits Student {
    String getJmbag();
}
