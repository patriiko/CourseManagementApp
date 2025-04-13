package hr.java.course.ostrunic.threads;

public class GetCurrentInstructorsCoursesThread extends DatabaseThread implements Runnable {
    private final Long instructorId;

    public GetCurrentInstructorsCoursesThread(Long instructorId) {
        this.instructorId = instructorId;
    }

    @Override
    public void run() {
        super.getCurrentInstructorsCoursesThread(instructorId);
    }
}
