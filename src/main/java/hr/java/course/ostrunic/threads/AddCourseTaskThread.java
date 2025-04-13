package hr.java.course.ostrunic.threads;

public class AddCourseTaskThread extends DatabaseThread implements Runnable {
    private final Long courseId;
    private final Long taskId;

    public AddCourseTaskThread(Long courseId, Long taskId) {
        this.courseId = courseId;
        this.taskId = taskId;
    }

    @Override
    public void run() {
        super.addCourseTaskThread(courseId, taskId);
    }
}
