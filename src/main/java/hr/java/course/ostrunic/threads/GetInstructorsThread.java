package hr.java.course.ostrunic.threads;

import hr.java.course.ostrunic.model.Instructor;

import java.util.ArrayList;
import java.util.List;

public class GetInstructorsThread extends DatabaseThread implements Runnable {
    private List<Instructor> instructors = new ArrayList<>();

    public List<Instructor> getInstructors() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return instructors;
    }

    @Override
    public void run() {
        instructors = super.getInstructorsThread();
    }
}
