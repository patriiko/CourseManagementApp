package hr.java.course.ostrunic.threads;

public class GetCertificatesForStudentThread extends DatabaseThread implements Runnable {
    private final Long studentId;

    public GetCertificatesForStudentThread(Long studentId) {
        this.studentId = studentId;
    }

    @Override
    public void run() {
        super.getCertificatesForStudentThread(studentId);
    }
}
