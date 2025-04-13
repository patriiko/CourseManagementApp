package hr.java.course.ostrunic.threads;

public class DeleteCourseThread extends DatabaseThread implements Runnable {
    private final Long id;

    public DeleteCourseThread(Long id) {
        this.id = id;
    }

    @Override
    public void run() {
        super.deleteCourseThread(id);
    }
}
