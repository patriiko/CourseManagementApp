package hr.java.course.ostrunic.model;

import hr.java.course.ostrunic.abstracts.SystemUser;
import hr.java.course.ostrunic.enums.UserType;
import hr.java.course.ostrunic.interfaces.AdminInterface;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

import static hr.java.course.ostrunic.main.Main.logger;

public non-sealed class Administrator extends SystemUser implements AdminInterface, Serializable {

    private List<Course> managedCourses;
    private List<Instructor> managedInstructors;

    public Administrator(Long id, String firstName, String lastName, String email, String username, String password, UserType userType, List<Course> managedCourses, List<Instructor> managedInstructors) {
        super(id, firstName, lastName, email, username, password, userType);
        this.managedCourses = managedCourses;
        this.managedInstructors = managedInstructors;
    }

    public Administrator(String username, String password) {
        super(username, password);
    }

    public Administrator(Long id, String firstName, String lastName, String email, String username, String password, UserType userType) {
        super(id, firstName, lastName, email, username, password, userType);
    }



    @Override
    public void addCourse(Course course) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("dat/admincourses.txt", true))) {
            writer.println("ID: " + course.getId());
            writer.println("Name: " + course.getName());
            writer.println();
        } catch (IOException e) {
            logger.error("Error while saving course to file: ", e);
        }
    }

    @Override
    public void removeCourse(Course course) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("dat/admincourses.txt", true))) {
            writer.println("Course with ID: " + course.getId() + " and Name: " + course.getName() + " has been removed.");
            writer.println();
        } catch (IOException e) {
            logger.error("Error while removing course from file: ", e);
        }
    }
}
