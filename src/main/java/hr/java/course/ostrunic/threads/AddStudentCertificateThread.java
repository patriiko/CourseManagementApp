package hr.java.course.ostrunic.threads;

public class AddStudentCertificateThread extends DatabaseThread implements Runnable {

    private final Long studentId;
    private final Long certificateId;

    public AddStudentCertificateThread(Long studentId, Long certificateId) {
        this.studentId = studentId;
        this.certificateId = certificateId;
    }

    @Override
    public void run() {
        super.addStudentCertificateThread(studentId, certificateId);
    }
}
