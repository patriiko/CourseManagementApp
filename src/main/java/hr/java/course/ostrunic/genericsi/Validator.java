package hr.java.course.ostrunic.genericsi;

import hr.java.course.ostrunic.exceptions.InvalidEmailException;
import hr.java.course.ostrunic.exceptions.InvalidPasswordException;

import java.util.regex.Pattern;

import static hr.java.course.ostrunic.main.Main.logger;

public class Validator<T> {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(".*@.*");

    public void validateEmail(T value) {
        if (value instanceof String) {
            String email = (String) value;
            if (!isValidEmailFormat(email)) {
                logger.error("Invalid e-mail format: " + email);
                throw new InvalidEmailException("Invalid e-mail format: " + email);
            }
        }
    }

    private boolean isValidEmailFormat(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public void validatePassword(T value) {
        if (value instanceof String) {
            String password = (String) value;
            if (!isValidPasswordFormat(password)) {
                logger.error("Invalid password format: " + password);
                throw new InvalidPasswordException("Password should contain min. 8 characters, 1 uppercase letter and 1 number.");
            }
        }
    }

    public boolean isValidPasswordFormat(String password) {

        if (password.length() < 8) {
            return false;
        }

        if (!containsUppercaseLetter(password)) {
            return false;
        }

        if (!containsNumber(password)) {
            return false;
        }

        return true;
    }

    private boolean containsUppercaseLetter(String password) {
        for (char character : password.toCharArray()) {
            if (Character.isUpperCase(character)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsNumber(String password) {
        for (char character : password.toCharArray()) {
            if (Character.isDigit(character)) {
                return true;
            }
        }
        return false;
    }
}
