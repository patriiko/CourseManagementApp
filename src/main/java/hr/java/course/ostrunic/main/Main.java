package hr.java.course.ostrunic.main;

import java.time.format.DateTimeFormatter;
import java.util.List;

import hr.java.course.ostrunic.enums.UserType;
import hr.java.course.ostrunic.model.Administrator;
import hr.java.course.ostrunic.model.Course;
import hr.java.course.ostrunic.model.Student;
import hr.java.course.ostrunic.records.CourseCategory;
import hr.java.course.ostrunic.utils.DatabaseUtils;
import hr.java.course.ostrunic.sort.SortByFirstName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static DateTimeFormatter formatterDATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        logger.info("Starting application");

        Administrator admin = DatabaseUtils.getAdministratorByUserType(UserType.ADMINISTRATOR);

        Course course1 = new Course(1L, "Java programiranje");
        admin.addCourse(course1);
        admin.removeCourse(course1);

        CourseCategory category = new CourseCategory(1, "Programiranje", "Tečajevi vezani za programiranje");
        logger.info("Category: " + category);

        Integer id = category.id();
        String name = category.name();
        String description = category.description();

        System.out.println("Category id: " + id);
        System.out.println("Category name: " + name);
        System.out.println("Category description: " + description);

        sortStudentsById();
    }

    private static void sortStudentsById() {
        List<Student> students = DatabaseUtils.getStudents();
        students.sort(new SortByFirstName());

        for (Student student : students) {
            System.out.println(student);
        }
    }
}
