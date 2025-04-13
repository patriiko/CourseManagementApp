package hr.java.course.ostrunic.threads;

import hr.java.course.ostrunic.model.Instructor;

public class UpdateInstructorThread extends DatabaseThread implements Runnable {
    private final Instructor instructor;

    public UpdateInstructorThread(Instructor updatedInstructor) {
        this.instructor = updatedInstructor;
    }

    @Override
    public void run() {
        super.updateInstructorThread(instructor);
    }
}
