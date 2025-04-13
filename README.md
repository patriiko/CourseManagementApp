# CourseManagementApp

A Java application for managing courses, students, tasks, and certificates. The project uses JavaFX for the GUI, data serialization for local storage, and follows the MVC architecture for code organization.

## Features

- Instructor and student login
- Course, task, and certificate management
- Save and load data using `.ser` files
- Easy user data management

## Project Structure

```
conf/                # Configuration files (e.g., database settings)
dat/                 # Serialized and text data files
logs/                # Log files
src/main/java/       # Main Java code (organized into packages)
src/main/resources/  # Resources (logback, styles)
```

## How to Run

1. Clone the repository:
    ```bash
    git clone https://github.com/YOUR_USERNAME/CourseManagementApp.git
    cd CourseManagementApp
    ```

2. Run the application from the `LoginApplication.java` class using your IDE (e.g., IntelliJ).

## Technologies

- Java 17
- JavaFX
- Maven
- Logback
- MVC architecture

### Note

Sensitive data such as passwords and configuration files should not be made public. Use `.gitignore` to prevent accidentally pushing them to GitHub.
