package hr.java.course.ostrunic.exceptions;

public class CourseNotFoundException extends Exception {
    public CourseNotFoundException(String message) {
        super(message);
    }

    public CourseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CourseNotFoundException(Throwable cause) {
        super(cause);
    }

    public CourseNotFoundException() {
    }
}
