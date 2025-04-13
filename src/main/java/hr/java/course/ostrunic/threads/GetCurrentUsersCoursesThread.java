package hr.java.course.ostrunic.threads;

public class GetCurrentUsersCoursesThread extends DatabaseThread implements Runnable {
    private final Long userId;

    public GetCurrentUsersCoursesThread(Long userId) {
        this.userId = userId;
    }

    @Override
    public void run() {
        super.getCurrentUsersCoursesThread(userId);
    }
}
