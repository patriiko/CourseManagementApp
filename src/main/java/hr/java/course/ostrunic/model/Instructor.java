package hr.java.course.ostrunic.model;

import hr.java.course.ostrunic.abstracts.SystemUser;
import hr.java.course.ostrunic.enums.UserType;

import java.io.Serializable;
import java.util.List;

public class Instructor extends SystemUser implements Serializable {

    private List<Course> coursesTaught;

    public Instructor(Long id, String firstName, String lastName, String email, String username, String password, UserType userType, List<Course> coursesTaught) {
        super(id, firstName, lastName, email, username, password, userType);
        this.coursesTaught = coursesTaught;
    }

    public Instructor(Long id, String firstName, String lastName, String email, String username, String password, UserType userType) {
        super(id, firstName, lastName, email, username, password, userType);
    }

    public Instructor(Long id, String firstName, String lastName, String email, String username, String password) {
        super(id, firstName, lastName, email, username, password);
    }

    public Instructor(String username, String password) {
        super(username, password);
    }

    public List<Course> getCoursesTaught() {
        return coursesTaught;
    }

    public void setCoursesTaught(List<Course> coursesTaught) {
        this.coursesTaught = coursesTaught;
    }

    @Override
    public String toString() {
        return "Instructor[ID = " + getId() + ", First Name = " + getFirstName() + ", Last Name = " + getLastName() + ", Email = " + getEmail() + ", Username = " + getUsername() + ", Password = " + getPassword() + ", UserType = " + getUserType() + "]";
    }
}
