package hr.java.course.ostrunic.threads;

public class DeleteTaskThread extends DatabaseThread implements Runnable {
    private final Long id;

    public DeleteTaskThread(Long id) {
        this.id = id;
    }

    @Override
    public void run() {
        super.deleteTaskThread(id);
    }
}
