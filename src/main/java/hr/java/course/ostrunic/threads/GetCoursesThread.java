package hr.java.course.ostrunic.threads;

import hr.java.course.ostrunic.model.Course;

import java.util.ArrayList;
import java.util.List;

public class GetCoursesThread extends DatabaseThread implements Runnable {

    private List<Course> courses = new ArrayList<>();

    public List<Course> getCourses() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return courses;
    }

    @Override
    public void run() {
        courses = super.getCoursesThread();
    }
}
