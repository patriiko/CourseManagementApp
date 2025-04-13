package hr.java.course.ostrunic.threads;

import hr.java.course.ostrunic.model.Course;

public class SaveCourseThread extends DatabaseThread implements Runnable {
    private final Course course;

    public SaveCourseThread(Course course) {
        this.course = course;
    }

    @Override
    public void run() {
        super.saveCourseThread(course);
    }
}
