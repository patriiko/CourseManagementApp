package hr.java.course.ostrunic.model;

import hr.java.course.ostrunic.abstracts.SystemUser;
import hr.java.course.ostrunic.enums.UserType;
import hr.java.course.ostrunic.interfaces.StudentInterface;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public final class Student extends SystemUser implements Serializable, StudentInterface {
    private String jmbag;
    private List<Course> coursesEnrolled;
    private Set<Certificate> certificates;

    public Student(Long id, String firstName, String lastName, String email, String username, String password, UserType userType, String jmbag, List<Course> enrolledCourses, Set<Certificate> certificates) {
        super(id, firstName, lastName, email, username, password, userType);
        this.jmbag = jmbag;
        this.coursesEnrolled = enrolledCourses;
        this.certificates = certificates;
    }

    public Student(Long id, String firstName, String lastName, String email, String username, String password, UserType userType, String jmbag, List<Course> enrolledCourses) {
        super(id, firstName, lastName, email, username, password, userType);
        this.jmbag = jmbag;
        this.coursesEnrolled = enrolledCourses;
    }

    public Student(Long id, String firstName, String lastName, String email, String username, String password, UserType userType, String jmbag) {
        super(id, firstName, lastName, email, username, password, userType);
        this.jmbag = jmbag;
    }

    public Student(String username, String password) {
        super(username, password);
    }

    public Student(Long id, String firstName, String lastName, String email, String username, String password, String jmbag) {
        super(id, firstName, lastName, email, username, password);
        this.jmbag = jmbag;
    }


    public void setJmbag(String jmbag) {
        this.jmbag = jmbag;
    }

    @Override
    public String getJmbag() {
        return jmbag;
    }

    public Set<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(Set<Certificate> certificates) {
        this.certificates = certificates;
    }

    public List<Course> getCourses() {
        return coursesEnrolled;
    }

    public void setCourses(List<Course> enrolledCourses) {
        this.coursesEnrolled = enrolledCourses;
    }

    @Override
    public String toString() {
        return "Student[id" + super.getId() + ", jmbag=" + jmbag + ", firstName=" + super.getFirstName() + ", lastName=" + super.getLastName() + ", email=" + super.getEmail() + ", username=" + super.getUsername() + ", password=" + super.getPassword() + "]";
    }
}
