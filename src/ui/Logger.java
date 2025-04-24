package ui;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final String LOG_FILE_PATH = "login_logs.txt";

    public static void logLogin(String username, String userType) {
        logEvent("Login", username, userType);
    }

    public static void logRegister(String username, String userType) {
        logEvent("Register", username, userType);
    }

    private static void logEvent(String eventType, String username, String userType) {
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = now.format(formatter);

            String logEntry = String.format("Event: %s, UserType: %s, Username: %s, Time: %s%n",
                    eventType, userType, username, formattedTime);

            writer.write(logEntry);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
