package hr.java.course.ostrunic.threads;

public class SaveStudentThread extends DatabaseThread implements Runnable {
    private final String jmbag;
    private final Long id;

    public SaveStudentThread(String jmbag, Long id) {
        this.jmbag = jmbag;
        this.id = id;
    }

    @Override
    public void run() {
        super.saveStudentThread(jmbag, id);
    }
}
