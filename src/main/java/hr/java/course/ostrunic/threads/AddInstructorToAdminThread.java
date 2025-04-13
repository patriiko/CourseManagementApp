package hr.java.course.ostrunic.threads;

public class AddInstructorToAdminThread extends DatabaseThread implements Runnable {
    private final Long instructorId;
    private final Long adminId;

    public AddInstructorToAdminThread(Long instructorId, Long adminId) {
        this.instructorId = instructorId;
        this.adminId = adminId;
    }

    @Override
    public void run() {
        super.addInstructorToAdministratorThread(instructorId, adminId);
    }
}
