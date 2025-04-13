package hr.java.course.ostrunic.threads;

import hr.java.course.ostrunic.model.Student;

import java.util.ArrayList;
import java.util.List;

public class GetStudentsThread extends DatabaseThread implements Runnable {
    private List<Student> students = new ArrayList<>();

    public List<Student> getStudents() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return students;
    }

    @Override
    public void run() {
        students = super.getStudentsThread();
    }

}
