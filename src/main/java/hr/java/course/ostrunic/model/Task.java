package hr.java.course.ostrunic.model;

import java.io.Serializable;

public class Task implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Boolean completed;

    public Task(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.completed = builder.completed;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String description;
        private Boolean completed;

        public Builder(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder completed(Boolean completed) {
            this.completed = completed;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isCompleted() {
        return completed;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                '}';
    }
}
