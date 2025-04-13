package hr.java.course.ostrunic.threads;

public class AddCourseToAdminThread extends DatabaseThread implements Runnable {
    private final Long adminId;
    private final Long courseId;

    public AddCourseToAdminThread(Long adminId, Long courseId) {
        this.adminId = adminId;
        this.courseId = courseId;
    }

    @Override
    public void run() {
        super.addCourseToAdministratorThread(adminId, courseId);
    }
}
