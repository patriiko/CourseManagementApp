package hr.java.course.ostrunic.threads;

public class DeleteStudentThread extends DatabaseThread implements Runnable {
    private final Long id;

    public DeleteStudentThread(Long id) {
        this.id = id;
    }

    @Override
    public void run() {
        super.deleteStudentThread(id);
    }
}
