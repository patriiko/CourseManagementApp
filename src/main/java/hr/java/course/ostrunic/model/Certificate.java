package hr.java.course.ostrunic.model;

import java.io.Serializable;
import java.util.Objects;

public class Certificate implements Serializable {
    private Long id;
    private String description;
    private Course course;
    private Long studentId;
    private Long courseId;

    public Certificate(Long id, String description, Course course) {
        this.id = id;
        this.description = description;
        this.course = course;
    }

    public Certificate(Long studentId, Long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public Certificate() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getCourseName() {
        return course.getName();
    }

    public String getName() {
        return description;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificate that = (Certificate) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(course, that.course) && Objects.equals(studentId, that.studentId) && Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, course, studentId, courseId);
    }
}
