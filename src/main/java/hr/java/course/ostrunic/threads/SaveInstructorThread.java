package hr.java.course.ostrunic.threads;

public class SaveInstructorThread extends DatabaseThread implements Runnable {
    private final Long id;

    public SaveInstructorThread(Long id) {
        this.id = id;
    }

    @Override
    public void run() {
        super.saveInstructorThread(id);
    }
}
