package hr.java.course.ostrunic.exceptions;

public class AlreadyTeachingCourseException extends Exception{
    public AlreadyTeachingCourseException(String message) {
        super(message);
    }

    public AlreadyTeachingCourseException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyTeachingCourseException(Throwable cause) {
        super(cause);
    }

    public AlreadyTeachingCourseException() {
    }
}
