package hr.java.course.ostrunic.utils;

import hr.java.course.ostrunic.enums.UserType;
import hr.java.course.ostrunic.exceptions.AlreadyTeachingCourseException;
import hr.java.course.ostrunic.exceptions.CourseNotFoundException;
import hr.java.course.ostrunic.exceptions.InvalidCourseEnrollmentException;
import hr.java.course.ostrunic.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Properties;

public class DatabaseUtils {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtils.class);
    private static final String DATABASE_FILE = "conf/database.properties";
    private static final String SELECT_COURSES_FOR_STUDENT = "SELECT * FROM STUDENT_COURSE SC, COURSE C WHERE SC.STUDENT_USER_ID = ? AND SC.COURSE_ID = C.ID";
    private static final String SELECT_COURSES_FOR_INSTRUCTOR = "SELECT * FROM INSTRUCTOR_COURSE IC, COURSE C WHERE IC.INSTRUCTOR_USER_ID = ? AND IC.COURSE_ID = C.ID";
    private static final String SELECT_TASKS_FOR_COURSE = "SELECT * FROM COURSE_TASK CT, TASK T WHERE CT.COURSE_ID = ? AND CT.TASK_ID = T.ID";


    private static Connection connectToDatabase() throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(DATABASE_FILE));

        String databaseUrl = properties.getProperty("databaseUrl");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        Connection connection = DriverManager.getConnection(databaseUrl, username, password);

        return connection;
    }

    private static void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error while closing the connection to the database: ", e);
        }
    }

    public static String getUserType(String username) {
        String userType = null;

        try (Connection connection = connectToDatabase()) {
            String query = "SELECT USER_TYPE FROM COURSE_SYSTEM_USER WHERE USERNAME = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userType = resultSet.getString("USER_TYPE");
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while getting an user type from the database: ", e);
        }

        return userType;
    }

    public static List<Student> getStudents() {
        List<Student> students = new ArrayList<>();

        try (Connection connection = connectToDatabase()) {
            String query = "SELECT * FROM COURSE_SYSTEM_USER JOIN STUDENT ON COURSE_SYSTEM_USER.ID = STUDENT.USER_ID";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                String firstName = resultSet.getString("FIRST_NAME");
                String lastName = resultSet.getString("LAST_NAME");
                String username = resultSet.getString("USERNAME");
                String email = resultSet.getString("EMAIL");
                String password = resultSet.getString("PASSWORD");
                String jmbag = resultSet.getString("JMBAG");

                List<Course> courses = loadCourses(id, SELECT_COURSES_FOR_STUDENT);

                Student student = new Student(id, firstName, lastName, email, username, password, UserType.STUDENT, jmbag, courses);
                students.add(student);
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while getting students from the database: ", e);
        }


        return students;
    }

    public static List<Course> loadCourses(Long userId, String query) {
        List<Course> courses = getCourses();
        List<Long> coursesId = new ArrayList<>();

        try (Connection connection = connectToDatabase()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                coursesId.add(resultSet.getLong("ID"));

        } catch (SQLException | IOException e) {
            logger.error("Error while getting courses from the database: ", e);
        }

        return courses.stream().filter(course -> coursesId.contains(course.getId())).toList();
    }

    public static List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();

        try (Connection connection = connectToDatabase()) {
            String query = "SELECT * FROM COURSE";
            Statement statement = connection.createStatement();
            statement.execute(query);
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");

                List<Task> tasks = loadTasks(id, SELECT_TASKS_FOR_COURSE);

                Course course = new Course(id, name, tasks);
                courses.add(course);
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while getting courses from the database: ", e);
        }

        return courses;
    }

    public static List<Task> loadTasks(Long userId, String query) {
        List<Task> tasks = getTasks();
        List<Long> tasksId = new ArrayList<>();

        try (Connection connection = connectToDatabase()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                tasksId.add(resultSet.getLong("ID"));

        } catch (SQLException | IOException e) {
            logger.error("Error while getting tasks from the database: ", e);
        }

        return tasks.stream().filter(task -> tasksId.contains(task.getId())).toList();
    }

    public static List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();

        try (Connection connection = connectToDatabase()) {
            String query = "SELECT * FROM TASK";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");
                String description = resultSet.getString("DESCRIPTION");
                Boolean completed = resultSet.getBoolean("COMPLETED");

                Task task = new Task.Builder(id, name)
                        .description(description)
                        .completed(completed)
                        .build();

                tasks.add(task);
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while getting tasks from the database: ", e);
        }

        return tasks;
    }

    public static List<Certificate> getCertificates() {
        List<Certificate> certificates = new ArrayList<>();
        String query = "SELECT c.*, co.NAME AS COURSE_NAME FROM CERTIFICATE c LEFT JOIN COURSE co ON c.COURSE_ID = co.ID";

        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Certificate certificate = new Certificate();
                certificate.setId(resultSet.getLong("ID"));
                certificate.setDescription(resultSet.getString("DESCRIPTION"));

                Course course = new Course();
                course.setName(resultSet.getString("COURSE_NAME"));
                certificate.setCourse(course);

                certificates.add(certificate);
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while getting all certificates: ", e);
        }

        return certificates;
    }

    public static List<Instructor> getInstructors() {
        List<Instructor> instructors = new ArrayList<>();

        try (Connection connection = connectToDatabase()) {
            String query = "SELECT * FROM COURSE_SYSTEM_USER JOIN INSTRUCTOR ON COURSE_SYSTEM_USER.ID = INSTRUCTOR.USER_ID";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                String firstName = resultSet.getString("FIRST_NAME");
                String lastName = resultSet.getString("LAST_NAME");
                String username = resultSet.getString("USERNAME");
                String email = resultSet.getString("EMAIL");
                String password = resultSet.getString("PASSWORD");

                List<Course> courses = loadCourses(id, SELECT_COURSES_FOR_INSTRUCTOR);

                Instructor instructor = new Instructor(id, firstName, lastName, email, username, password, UserType.INSTRUCTOR, courses);
                instructors.add(instructor);
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while getting instructors from the database: ", e);
        }

        return instructors;
    }

    public static void saveSystemUser(String firstName, String lastName, String username, String email, String password, String userType) {
        try (Connection connection = connectToDatabase()) {
            String query = "INSERT INTO COURSE_SYSTEM_USER (FIRST_NAME, LAST_NAME, USERNAME, EMAIL, PASSWORD, USER_TYPE) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, password);
            preparedStatement.setString(6, userType);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while saving new system user to the database: ", e);
        }
    }

    public static void saveStudent(String JMBAG, Long userId) {
        try (Connection connection = connectToDatabase()) {
            String query = "INSERT INTO STUDENT (JMBAG, USER_ID) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, JMBAG);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while saving new student to the database: ", e);
        }
    }

    public static void saveInstructor(Long userId) {
        try (Connection connection = connectToDatabase()) {
            String query = "INSERT INTO INSTRUCTOR (USER_ID) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while saving new instructor to the database: ", e);
        }
    }

    public static Long getNextUserId() {
        try (Connection connection = connectToDatabase();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT COALESCE(MAX(ID), 0) FROM COURSE_SYSTEM_USER");

            if (resultSet.next()) {
                Long maxUserId = resultSet.getLong(1);
                return maxUserId + 1;
            }

        } catch (SQLException | IOException e) {
            logger.error("Error while getting next User ID from database [" + e + "]");
            System.out.println("Error while getting next User ID from database [" + e + "]");
        }

        // Default value if something goes wrong
        return 1L;
    }

    public static long getUserId(String username) {
        long userId = -1;

        try (Connection connection = connectToDatabase()) {
            String query = "SELECT ID FROM COURSE_SYSTEM_USER WHERE USERNAME = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getLong("ID");
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while getting user ID from the database: ", e);
        }

        return userId;
    }

    public static Long getNextTaskId() {
        Long nextTaskId = null;

        try (Connection connection = connectToDatabase()) {
            String query = "SELECT MAX(ID) AS MAX_ID FROM TASK";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nextTaskId = resultSet.getLong("MAX_ID") + 1;
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while getting next task ID from the database: ", e);
        }

        return nextTaskId;
    }

    public static void saveTask(Task task) {
        try (Connection connection = connectToDatabase()) {
            String query = "INSERT INTO TASK (NAME, DESCRIPTION, COMPLETED) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setBoolean(3, task.isCompleted());

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while saving task to the database: ", e);
        }
    }

    public static void saveCourse(Course course) {
        try (Connection connection = connectToDatabase()) {
            String query = "INSERT INTO COURSE (NAME) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, course.getName());
            preparedStatement.executeUpdate();

            addCertificateForNewCourse(course);
        } catch (SQLException | IOException e) {
            logger.error("Error while adding course to the database: ", e);
        }
    }

    public static void addCourseTask(Long courseId, Long taskId) {
        try (Connection connection = connectToDatabase()) {
            String query = "INSERT INTO COURSE_TASK (COURSE_ID, TASK_ID) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, courseId);
            preparedStatement.setLong(2, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while adding task to course in the database: ", e);
        }
    }

    public static void addCertificateForNewCourse(Course course) {
        String query = "INSERT INTO CERTIFICATE (DESCRIPTION, COURSE_ID) VALUES (?, ?)";

        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            String certificateDescription = "Certifikat: " + course.getName();
            preparedStatement.setString(1, certificateDescription);
            preparedStatement.setLong(2, course.getId());

            Certificate certificate = new Certificate();
            certificate.setDescription(certificateDescription);
            certificate.setCourse(course);
            SerializationUtils.serializeObject(certificate, "dat/certificates.ser");

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while adding a certificate for the new course: ", e);
        }
    }

    public static Long getNextCourseId() {
        try (Connection connection = connectToDatabase();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT COALESCE(MAX(ID), 0) FROM COURSE");

            if (resultSet.next()) {
                Long maxCourseId = resultSet.getLong(1);
                return maxCourseId + 1;
            }

        } catch (SQLException | IOException e) {
            logger.error("Error while getting next Factory ID from database [" + e + "]");
            System.out.println("Error while getting next Factory ID from database [" + e + "]");
        }

        return 1L;
    }

    public static void addCourseToAdministrator(Long administratorId, Long courseId) {
        String query = "INSERT INTO ADMINISTRATOR_COURSE (ADMINISTRATOR_USER_ID, COURSE_ID) VALUES (?, ?)";

        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, administratorId);
            preparedStatement.setLong(2, courseId);

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while adding a course to the administrator: ", e);
        }
    }

    public static void addStudentToAdministrator(Long administratorId, Long studentId) {
        String query = "INSERT INTO ADMINISTRATOR_STUDENT (ADMINISTRATOR_USER_ID, STUDENT_USER_ID) VALUES (?, ?)";

        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, administratorId);
            preparedStatement.setLong(2, studentId);

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while adding a student to the administrator: ", e);
        }
    }

    public static void addInstructorToAdministrator(Long administratorId, Long instructorId) {
        String query = "INSERT INTO ADMINISTRATOR_INSTRUCTOR (ADMINISTRATOR_USER_ID, INSTRUCTOR_USER_ID) VALUES (?, ?)";

        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, administratorId);
            preparedStatement.setLong(2, instructorId);

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while adding a student to the administrator: ", e);
        }
    }

    public static void beginLecturing(Long instructorId, Course course) throws AlreadyTeachingCourseException, CourseNotFoundException {
        if (course == null) {
            logger.error("Course not found");
            throw new CourseNotFoundException("Course not found");
        }

        if (!isLecturingValid(instructorId, course.getId())) {
            logger.error("Invalid teaching course enrollment");
            throw new AlreadyTeachingCourseException("Invalid teaching course enrollment");
        }

        try (Connection connection = connectToDatabase()) {
            String query = "INSERT INTO INSTRUCTOR_COURSE (INSTRUCTOR_USER_ID, COURSE_ID) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, instructorId);
            preparedStatement.setLong(2, course.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while beginning lecturing: ", e);
        }
    }

    private static boolean isLecturingValid(Long instructorId, Long courseId) {
        List<Course> currentCourses = getCurrentInstructorsCourses(instructorId);
        for (Course course : currentCourses) {
            if (course.getId().equals(courseId)) {
                return false;
            }
        }
        return true;
    }

    public static List<Course> getCurrentInstructorsCourses(Long instructorId) {
        List<Course> courses = new ArrayList<>();

        try (Connection connection = connectToDatabase()) {
            String query = "SELECT * FROM INSTRUCTOR_COURSE IC, COURSE C WHERE IC.INSTRUCTOR_USER_ID = ? AND IC.COURSE_ID = C.ID";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, instructorId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");

                List<Task> tasks = loadTasks(id, SELECT_TASKS_FOR_COURSE);

                Course course = new Course(id, name, tasks);
                courses.add(course);
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while getting courses for instructor from the database: ", e);
        }

        return courses;
    }

    public static void enrollInCourse(Long studentId, Course course) throws InvalidCourseEnrollmentException, CourseNotFoundException {
        if (course == null) {
            logger.error("Course not found");
            throw new CourseNotFoundException("Course not found");
        }

        if (!isEnrollmentValid(studentId, course.getId())) {
            logger.error("Invalid course enrollment");
            throw new InvalidCourseEnrollmentException("Invalid course enrollment");
        }

        try (Connection connection = connectToDatabase()) {
            String query = "INSERT INTO STUDENT_COURSE (STUDENT_USER_ID, COURSE_ID) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, studentId);
            preparedStatement.setLong(2, course.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while enrolling in course: ", e);
        }
    }

    public static boolean isEnrollmentValid(Long studentId, Long courseId) {
        List<Course> currentCourses = getCurrentUsersCourses(studentId);
        for (Course course : currentCourses) {
            if (course.getId().equals(courseId)) {
                return false;
            }
        }
        return true;
    }

    public static List<Course> getCurrentUsersCourses(Long studentId) {
        List<Course> courses = new ArrayList<>();

        try (Connection connection = connectToDatabase()) {
            String query = "SELECT * FROM STUDENT_COURSE SC, COURSE C WHERE SC.STUDENT_USER_ID = ? AND SC.COURSE_ID = C.ID";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");

                List<Task> tasks = loadTasks(id, SELECT_TASKS_FOR_COURSE);

                Course course = new Course(id, name, tasks);
                courses.add(course);
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while getting courses for student from the database: ", e);
        }

        return courses;
    }

    public static void markCourseAsCompleted(Long studentId, Long courseId) {
        try (Connection connection = connectToDatabase()) {
            String deleteQuery = "DELETE FROM STUDENT_COURSE WHERE STUDENT_USER_ID = ? AND COURSE_ID = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setLong(1, studentId);
            deleteStatement.setLong(2, courseId);
            deleteStatement.executeUpdate();

            String insertQuery = "INSERT INTO COMPLETED_COURSES (STUDENT_ID, COURSE_ID) VALUES (?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setLong(1, studentId);
            insertStatement.setLong(2, courseId);
            insertStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while marking course as completed: ", e);
        }
    }

    public static Set<Certificate> getCertificatesForStudent(Long studentId) {
        Set<Certificate> certificates = new HashSet<>();

        try (Connection connection = connectToDatabase()) {
            String query = "SELECT C.*, CO.NAME AS COURSE_NAME FROM STUDENT_CERTIFICATE SC JOIN CERTIFICATE C ON SC.CERTIFICATE_ID = C.ID JOIN COURSE CO ON C.COURSE_ID = CO.ID WHERE SC.STUDENT_USER_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Certificate certificate = new Certificate();
                certificate.setId(resultSet.getLong("ID"));
                certificate.setDescription(resultSet.getString("DESCRIPTION"));

                Course course = new Course();
                course.setId(resultSet.getLong("COURSE_ID"));
                course.setName(resultSet.getString("COURSE_NAME"));
                certificate.setCourse(course);

                certificates.add(certificate);
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while getting certificates for student from the database: ", e);
        }

        return certificates;
    }

    public static void addStudentCertificate(Long studentId, Long courseId) {
        try (Connection connection = connectToDatabase()) {
            String query = "INSERT INTO STUDENT_CERTIFICATE (STUDENT_USER_ID, CERTIFICATE_ID) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, studentId);
            preparedStatement.setLong(2, courseId);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while adding student certificate: ", e);
        }
    }

    public static Administrator getAdministratorByUserType(UserType userType) {
        Administrator administrator = null;

        try (Connection connection = connectToDatabase()) {
            String query = "SELECT * FROM COURSE_SYSTEM_USER JOIN COURSE_ADMINISTRATOR ON COURSE_SYSTEM_USER.ID = COURSE_ADMINISTRATOR.USER_ID WHERE USER_TYPE = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userType.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                String firstName = resultSet.getString("FIRST_NAME");
                String lastName = resultSet.getString("LAST_NAME");
                String username = resultSet.getString("USERNAME");
                String email = resultSet.getString("EMAIL");
                String password = resultSet.getString("PASSWORD");

                administrator = new Administrator(id, firstName, lastName, email, username, password, userType);
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while getting administrator from the database: ", e);
        }

        return administrator;
    }

    public static void deleteStudent(Long studentId) {
        try (Connection connection = connectToDatabase()) {
            connection.setAutoCommit(false);

            String deleteAdministratorStudentQuery = "DELETE FROM ADMINISTRATOR_STUDENT WHERE STUDENT_USER_ID = ?";
            PreparedStatement deleteAdministratorStudentStatement = connection.prepareStatement(deleteAdministratorStudentQuery);
            deleteAdministratorStudentStatement.setLong(1, studentId);
            deleteAdministratorStudentStatement.executeUpdate();

            String deleteStudentCourseQuery = "DELETE FROM STUDENT_COURSE WHERE STUDENT_USER_ID = ?";
            PreparedStatement deleteStudentCourseStatement = connection.prepareStatement(deleteStudentCourseQuery);
            deleteStudentCourseStatement.setLong(1, studentId);
            deleteStudentCourseStatement.executeUpdate();

            String deleteStudentCertificateQuery = "DELETE FROM STUDENT_CERTIFICATE WHERE STUDENT_USER_ID = ?";
            PreparedStatement deleteStudentCertificateStatement = connection.prepareStatement(deleteStudentCertificateQuery);
            deleteStudentCertificateStatement.setLong(1, studentId);
            deleteStudentCertificateStatement.executeUpdate();

            String deleteCompletedCoursesQuery = "DELETE FROM COMPLETED_COURSES WHERE STUDENT_ID = ?";
            PreparedStatement deleteCompletedCoursesStatement = connection.prepareStatement(deleteCompletedCoursesQuery);
            deleteCompletedCoursesStatement.setLong(1, studentId);
            deleteCompletedCoursesStatement.executeUpdate();

            String deleteStudentQuery = "DELETE FROM STUDENT WHERE USER_ID = ?";
            PreparedStatement deleteStudentStatement = connection.prepareStatement(deleteStudentQuery);
            deleteStudentStatement.setLong(1, studentId);
            deleteStudentStatement.executeUpdate();

            String deleteCourseSystemUserQuery = "DELETE FROM COURSE_SYSTEM_USER WHERE ID = ?";
            PreparedStatement deleteCourseSystemUserStatement = connection.prepareStatement(deleteCourseSystemUserQuery);
            deleteCourseSystemUserStatement.setLong(1, studentId);
            deleteCourseSystemUserStatement.executeUpdate();

            connection.commit();
        } catch (SQLException | IOException e) {
            logger.error("Error while deleting student from the database: ", e);
        }
    }

    public static void deleteInstructor(Long instructorId) {
        try (Connection connection = connectToDatabase()) {
            connection.setAutoCommit(false);

            String deleteAdministratorInstructorQuery = "DELETE FROM ADMINISTRATOR_INSTRUCTOR WHERE INSTRUCTOR_USER_ID = ?";
            PreparedStatement deleteAdministratorInstructorStatement = connection.prepareStatement(deleteAdministratorInstructorQuery);
            deleteAdministratorInstructorStatement.setLong(1, instructorId);
            deleteAdministratorInstructorStatement.executeUpdate();

            String deleteInstructorCourseQuery = "DELETE FROM INSTRUCTOR_COURSE WHERE INSTRUCTOR_USER_ID = ?";
            PreparedStatement deleteInstructorCourseStatement = connection.prepareStatement(deleteInstructorCourseQuery);
            deleteInstructorCourseStatement.setLong(1, instructorId);
            deleteInstructorCourseStatement.executeUpdate();

            String deleteInstructorQuery = "DELETE FROM INSTRUCTOR WHERE USER_ID = ?";
            PreparedStatement deleteInstructorStatement = connection.prepareStatement(deleteInstructorQuery);
            deleteInstructorStatement.setLong(1, instructorId);
            deleteInstructorStatement.executeUpdate();

            String deleteCourseSystemUserQuery = "DELETE FROM COURSE_SYSTEM_USER WHERE ID = ?";
            PreparedStatement deleteCourseSystemUserStatement = connection.prepareStatement(deleteCourseSystemUserQuery);
            deleteCourseSystemUserStatement.setLong(1, instructorId);
            deleteCourseSystemUserStatement.executeUpdate();

            connection.commit();
        } catch (SQLException | IOException e) {
            logger.error("Error while deleting instructor from the database: ", e);
        }
    }

    public static void deleteCourse(Long courseId) {
        try (Connection connection = connectToDatabase()) {
            connection.setAutoCommit(false);

            String deleteStudentCertificateQuery = "DELETE FROM STUDENT_CERTIFICATE WHERE CERTIFICATE_ID IN (SELECT ID FROM CERTIFICATE WHERE COURSE_ID = ?)";
            PreparedStatement deleteStudentCertificateStatement = connection.prepareStatement(deleteStudentCertificateQuery);
            deleteStudentCertificateStatement.setLong(1, courseId);
            deleteStudentCertificateStatement.executeUpdate();

            String deleteCertificateQuery = "DELETE FROM CERTIFICATE WHERE COURSE_ID = ?";
            PreparedStatement deleteCertificateStatement = connection.prepareStatement(deleteCertificateQuery);
            deleteCertificateStatement.setLong(1, courseId);
            deleteCertificateStatement.executeUpdate();

            String deleteCourseTaskQuery = "DELETE FROM COURSE_TASK WHERE COURSE_ID = ?";
            PreparedStatement deleteCourseTaskStatement = connection.prepareStatement(deleteCourseTaskQuery);
            deleteCourseTaskStatement.setLong(1, courseId);
            deleteCourseTaskStatement.executeUpdate();

            String deleteStudentCourseQuery = "DELETE FROM STUDENT_COURSE WHERE COURSE_ID = ?";
            PreparedStatement deleteStudentCourseStatement = connection.prepareStatement(deleteStudentCourseQuery);
            deleteStudentCourseStatement.setLong(1, courseId);
            deleteStudentCourseStatement.executeUpdate();

            String deleteCompletedCoursesQuery = "DELETE FROM COMPLETED_COURSES WHERE COURSE_ID = ?";
            PreparedStatement deleteCompletedCoursesStatement = connection.prepareStatement(deleteCompletedCoursesQuery);
            deleteCompletedCoursesStatement.setLong(1, courseId);
            deleteCompletedCoursesStatement.executeUpdate();

            String deleteInstructorCourseQuery = "DELETE FROM INSTRUCTOR_COURSE WHERE COURSE_ID = ?";
            PreparedStatement deleteInstructorCourseStatement = connection.prepareStatement(deleteInstructorCourseQuery);
            deleteInstructorCourseStatement.setLong(1, courseId);
            deleteInstructorCourseStatement.executeUpdate();

            String deleteAdministratorCourseQuery = "DELETE FROM ADMINISTRATOR_COURSE WHERE COURSE_ID = ?";
            PreparedStatement deleteAdministratorCourseStatement = connection.prepareStatement(deleteAdministratorCourseQuery);
            deleteAdministratorCourseStatement.setLong(1, courseId);
            deleteAdministratorCourseStatement.executeUpdate();

            String deleteCourseQuery = "DELETE FROM COURSE WHERE ID = ?";
            PreparedStatement deleteCourseStatement = connection.prepareStatement(deleteCourseQuery);
            deleteCourseStatement.setLong(1, courseId);
            deleteCourseStatement.executeUpdate();

            connection.commit();
        } catch (SQLException | IOException e) {
            logger.error("Error while deleting course from the database: ", e);
        }
    }

    public static void deleteTask(Long taskId) {
        try (Connection connection = connectToDatabase()) {
            connection.setAutoCommit(false);

            String deleteCourseTaskQuery = "DELETE FROM COURSE_TASK WHERE TASK_ID = ?";
            PreparedStatement deleteCourseTaskStatement = connection.prepareStatement(deleteCourseTaskQuery);
            deleteCourseTaskStatement.setLong(1, taskId);
            deleteCourseTaskStatement.executeUpdate();

            String deleteTaskQuery = "DELETE FROM TASK WHERE ID = ?";
            PreparedStatement deleteTaskStatement = connection.prepareStatement(deleteTaskQuery);
            deleteTaskStatement.setLong(1, taskId);
            deleteTaskStatement.executeUpdate();

            connection.commit();
        } catch (SQLException | IOException e) {
            logger.error("Error while deleting task from the database: ", e);
        }
    }

    public static void updateTask(Task updatedTask) {
        try (Connection connection = connectToDatabase()) {
            String query = "UPDATE TASK SET NAME = ?, DESCRIPTION = ? WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, updatedTask.getName());
            preparedStatement.setString(2, updatedTask.getDescription());
            preparedStatement.setLong(3, updatedTask.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while updating task in the database: ", e);
        }
    }

    public static void updateCourse(Course updatedCourse) {
        try (Connection connection = connectToDatabase()) {
            String query = "UPDATE COURSE SET NAME = ? WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, updatedCourse.getName());
            preparedStatement.setLong(2, updatedCourse.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while updating course in the database: ", e);
        }
    }

    public static String getUsernameById(Long loggedInUserId) {
        String username = null;

        try (Connection connection = connectToDatabase()) {
            String query = "SELECT USERNAME FROM COURSE_SYSTEM_USER WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, loggedInUserId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                username = resultSet.getString("USERNAME");
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while getting username by ID from the database: ", e);
        }

        return username;
    }

    public static void updateInstructor(Instructor newInstructor) {
        try (Connection connection = connectToDatabase()) {
            String query = "UPDATE COURSE_SYSTEM_USER SET FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ?, USERNAME = ?, PASSWORD = ? WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newInstructor.getFirstName());
            preparedStatement.setString(2, newInstructor.getLastName());
            preparedStatement.setString(3, newInstructor.getEmail());
            preparedStatement.setString(4, newInstructor.getUsername());
            preparedStatement.setString(5, newInstructor.getPassword());
            preparedStatement.setLong(6, newInstructor.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while updating instructor in the database: ", e);
        }
    }

    public static void updateStudent(Student newStudent) {
        try (Connection connection = connectToDatabase()) {
            String query = "UPDATE COURSE_SYSTEM_USER SET FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ?, USERNAME = ?, PASSWORD = ? WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newStudent.getFirstName());
            preparedStatement.setString(2, newStudent.getLastName());
            preparedStatement.setString(3, newStudent.getEmail());
            preparedStatement.setString(4, newStudent.getUsername());
            preparedStatement.setString(5, newStudent.getPassword());
            preparedStatement.setLong(6, newStudent.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while updating student in the database: ", e);
        }
    }
}
