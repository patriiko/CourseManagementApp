package hr.java.course.ostrunic.exceptions;

public class InvalidCourseEnrollmentException extends Exception {
    public InvalidCourseEnrollmentException(String message) {
        super(message);
    }

    public InvalidCourseEnrollmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCourseEnrollmentException(Throwable cause) {
        super(cause);
    }

    public InvalidCourseEnrollmentException() {
    }
}
