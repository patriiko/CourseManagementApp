package hr.java.course.ostrunic.records;

import java.util.Objects;

import static hr.java.course.ostrunic.main.Main.logger;

public record CourseCategory(Integer id, String name, String description) {
    public CourseCategory {
        if (name == null || name.isBlank()) {
            logger.error("Name cannot be null or blank");
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            logger.error("Description cannot be null or blank");
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseCategory that = (CourseCategory) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "CourseCategory[" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ']';
    }
}
