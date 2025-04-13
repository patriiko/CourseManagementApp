package hr.java.course.ostrunic.threads;

import hr.java.course.ostrunic.exceptions.AlreadyTeachingCourseException;
import hr.java.course.ostrunic.exceptions.CourseNotFoundException;
import hr.java.course.ostrunic.model.Course;

import static hr.java.course.ostrunic.main.Main.logger;

public class BeginLecturingThread extends DatabaseThread implements Runnable {
    private final Long instructorId;
    private final Course course;

    public BeginLecturingThread(Long instructorId, Course course) {
        this.instructorId = instructorId;
        this.course = course;
    }

    @Override
    public void run() {
        try {
            super.beginLecturingThread(instructorId, course);
        } catch (AlreadyTeachingCourseException e) {
            logger.error("Instructor is already teaching this course");
            throw new RuntimeException(e);
        } catch (CourseNotFoundException e) {
            logger.error("Course not found");
            throw new RuntimeException(e);
        }
    }

}
