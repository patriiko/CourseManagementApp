package hr.java.course.ostrunic.threads;

public class DeleteInstructorThread extends DatabaseThread implements Runnable {
    private final Long id;

    public DeleteInstructorThread(Long id) {
        this.id = id;
    }

    @Override
    public void run() {
        super.deleteInstructorThread(id);
    }
}
