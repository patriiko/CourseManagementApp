package hr.java.course.ostrunic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
    private Long id;
    private String name;
    private List<Task> tasks;

    public Course(Long id, String name, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
    }

    public Course(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Course(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    public Course(String name) {
        this.name = name;
    }

    public Course() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void printCourseName() {
        System.out.println(this.name);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tasks=" + tasks +
                '}';
    }
}
