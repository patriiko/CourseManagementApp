package hr.java.course.ostrunic.threads;

import hr.java.course.ostrunic.model.Course;

public class UpdateCourseThread extends DatabaseThread implements Runnable {
    private final Course course;

    public UpdateCourseThread(Course updatedCourse) {
        this.course = updatedCourse;
    }

    @Override
    public void run() {
        super.updateCourseThread(course);
    }
}
