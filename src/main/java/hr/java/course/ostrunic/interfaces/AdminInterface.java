package hr.java.course.ostrunic.interfaces;

import hr.java.course.ostrunic.model.Administrator;
import hr.java.course.ostrunic.model.Course;
import hr.java.course.ostrunic.model.Instructor;

public sealed interface AdminInterface permits Administrator {
    void addCourse(Course course);
    void removeCourse(Course course);
}
