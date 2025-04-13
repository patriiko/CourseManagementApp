package hr.java.course.ostrunic.threads;

import hr.java.course.ostrunic.model.Student;

public class UpdateStudentThread extends DatabaseThread implements Runnable {
    private final Student student;

    public UpdateStudentThread(Student updatedStudent) {
        this.student = updatedStudent;
    }

    @Override
    public void run() {
        super.updateStudentThread(student);
    }
}
