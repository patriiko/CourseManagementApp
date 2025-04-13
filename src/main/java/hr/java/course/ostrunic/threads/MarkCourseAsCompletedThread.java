package hr.java.course.ostrunic.threads;

public class MarkCourseAsCompletedThread extends DatabaseThread implements Runnable {
    private final Long studentId;
    private final Long courseId;

    public MarkCourseAsCompletedThread(Long studentId, Long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    @Override
    public void run() {
        super.markCourseAsCompletedThread(studentId, courseId);
    }
}
