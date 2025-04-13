package hr.java.course.ostrunic.threads;

import hr.java.course.ostrunic.enums.UserType;
import hr.java.course.ostrunic.exceptions.AlreadyTeachingCourseException;
import hr.java.course.ostrunic.exceptions.CourseNotFoundException;
import hr.java.course.ostrunic.exceptions.InvalidCourseEnrollmentException;
import hr.java.course.ostrunic.model.*;
import hr.java.course.ostrunic.utils.DatabaseUtils;

import java.util.List;
import java.util.Set;

import static hr.java.course.ostrunic.main.Main.logger;

public abstract class DatabaseThread {
    public static Boolean isDatabaseOperationInProgress = false;

    public synchronized String getUserTypeThread(String username) {
        String userType;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        userType = DatabaseUtils.getUserType(username);
        isDatabaseOperationInProgress = false;
        notifyAll();
        return userType;
    }

    public synchronized long getUserIdThread(String username) {
        long userId;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        userId = DatabaseUtils.getUserId(username);
        isDatabaseOperationInProgress = false;
        notifyAll();
        return userId;
    }

    public synchronized List<Student> getStudentsThread() {
        List<Student> students;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        students = DatabaseUtils.getStudents();
        isDatabaseOperationInProgress = false;
        notifyAll();
        return students;
    }

    public synchronized List<Course> getCoursesThread() {
        List<Course> courses;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        courses = DatabaseUtils.getCourses();
        isDatabaseOperationInProgress = false;
        notifyAll();
        return courses;
    }

    public synchronized List<Task> getTasksThread() {
        List<Task> tasks;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        tasks = DatabaseUtils.getTasks();
        isDatabaseOperationInProgress = false;
        notifyAll();
        return tasks;
    }

    public synchronized List<Certificate> getCertificatesThread() {
        List<Certificate> certificates;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        certificates = DatabaseUtils.getCertificates();
        isDatabaseOperationInProgress = false;
        notifyAll();
        return certificates;
    }

    public synchronized List<Instructor> getInstructorsThread() {
        List<Instructor> instructors;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        instructors = DatabaseUtils.getInstructors();
        isDatabaseOperationInProgress = false;
        notifyAll();
        return instructors;
    }

    public synchronized List<Course> getCurrentInstructorsCoursesThread(Long instructorId) {
        List<Course> courses;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        courses = DatabaseUtils.getCurrentInstructorsCourses(instructorId);
        isDatabaseOperationInProgress = false;
        notifyAll();
        return courses;
    }

    public synchronized List<Course> getCurrentUsersCoursesThread(Long studentId) {
        List<Course> courses;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        courses = DatabaseUtils.getCurrentUsersCourses(studentId);
        isDatabaseOperationInProgress = false;
        notifyAll();
        return courses;
    }

    public synchronized Set<Certificate> getCertificatesForStudentThread(Long studentId) {
        Set<Certificate> certificates;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        certificates = DatabaseUtils.getCertificatesForStudent(studentId);
        isDatabaseOperationInProgress = false;
        notifyAll();
        return certificates;
    }

    public synchronized void saveSystemUserThread(String firstName, String lastName, String username, String email, String password, String userType) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.saveSystemUser(firstName, lastName, username, email, password, userType);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void saveStudentThread(String JMBAG, Long userId) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.saveStudent(JMBAG, userId);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void saveInstructorThread(Long userId) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.saveInstructor(userId);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void saveTaskThread(Task task) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.saveTask(task);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void saveCourseThread(Course course) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.saveCourse(course);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void addCourseTaskThread(Long courseId, Long taskId) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.addCourseTask(courseId, taskId);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void addStudentCertificateThread(Long studentId, Long courseId) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.addStudentCertificate(studentId, courseId);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void addCourseToAdministratorThread(Long administratorId, Long courseId) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.addCourseToAdministrator(administratorId, courseId);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void addStudentToAdministratorThread(Long administratorId, Long studentId) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.addStudentToAdministrator(administratorId, studentId);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void addInstructorToAdministratorThread(Long administratorId, Long instructorId) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.addInstructorToAdministrator(administratorId, instructorId);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void beginLecturingThread(Long instructorId, Course course) throws AlreadyTeachingCourseException, CourseNotFoundException {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.beginLecturing(instructorId, course);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void enrollInCourseThread(Long studentId, Course course) throws CourseNotFoundException, InvalidCourseEnrollmentException {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.enrollInCourse(studentId, course);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void markCourseAsCompletedThread(Long studentId, Long courseId) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.markCourseAsCompleted(studentId, courseId);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void deleteStudentThread(Long studentId) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.deleteStudent(studentId);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void deleteInstructorThread(Long instructorId) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.deleteInstructor(instructorId);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void deleteCourseThread(Long courseId) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.deleteCourse(courseId);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void deleteTaskThread(Long taskId) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.deleteTask(taskId);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void updateTaskThread(Task updatedTask) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.updateTask(updatedTask);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void updateCourseThread(Course updatedCourse) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.updateCourse(updatedCourse);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void updateStudentThread(Student updatedStudent) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.updateStudent(updatedStudent);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void updateInstructorThread(Instructor updatedInstructor) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.error("Error occurred while waiting for database operation to finish");
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.updateInstructor(updatedInstructor);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }
}
