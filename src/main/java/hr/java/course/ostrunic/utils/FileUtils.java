package hr.java.course.ostrunic.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private static final String FILE_NAME = "dat/userData.txt";

    public static void deleteUserCredentials(String username, String password) {
        List<String> lines = new ArrayList<>();
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            while ((line = reader.readLine()) != null) {
                if (!line.contains(username) || !line.contains(password)) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String newLine : lines) {
                writer.write(newLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean findUserCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] credentials = line.split(" ");
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true;
                }
            }
        }
        catch (IOException ex) {
            logger.error("Error while loading a file.", ex);
        }
        return false;
    }

    public static void saveUserCredentials(String username, String encryptedPassword) {
        try (FileWriter fileWriter = new FileWriter(FILE_NAME, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            printWriter.println(username + " " + encryptedPassword);
        } catch (IOException e) {
            logger.error("Error while saving user credentials to a file.", e);
        }
    }

    public static boolean userExists(String username) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length > 0 && parts[0].equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isUsernameTaken(String username) {
        Map<String, String> credentials = readCredentials();
        return credentials.containsKey(username);
    }

    public static void updateUserCredentials(String oldUsername, String newUsername, String newPassword) {
        Map<String, String> credentials = readCredentials();

        if (credentials.containsKey(oldUsername)) {
            credentials.remove(oldUsername);
            credentials.put(newUsername, newPassword);
        } else {
            credentials.put(newUsername, newPassword);
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, String> entry : credentials.entrySet()) {
                writer.println(entry.getKey() + " " + entry.getValue());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while updating user credentials", e);
        }
    }

    private static Map<String, String> readCredentials() {
        Map<String, String> credentials = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    credentials.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while reading user credentials", e);
        }
        return credentials;
    }
}
