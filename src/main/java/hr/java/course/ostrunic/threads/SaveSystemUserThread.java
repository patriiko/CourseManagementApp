package hr.java.course.ostrunic.threads;

public class SaveSystemUserThread extends DatabaseThread implements Runnable {
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String email;
    private final String password;

    private final String userType;

    public SaveSystemUserThread(String firstName, String lastName, String username, String email, String password, String userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    @Override
    public void run() {
        super.saveSystemUserThread(firstName, lastName, username, email, password, userType);
    }
}
