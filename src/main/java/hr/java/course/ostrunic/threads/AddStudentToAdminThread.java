package hr.java.course.ostrunic.threads;

public class AddStudentToAdminThread extends DatabaseThread implements Runnable {
    private final Long adminId;
    private final Long studentId;

    public AddStudentToAdminThread(Long adminId, Long studentId) {
        this.adminId = adminId;
        this.studentId = studentId;
    }

    @Override
    public void run() {
        super.addStudentToAdministratorThread(adminId, studentId);
    }
}
