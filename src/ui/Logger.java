package ui;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final String LOG_FILE_PATH = "login_logs.txt";

    // Custom log format and color styling
    private static final String LOG_HEADER_STYLE = "font-family: Arial, sans-serif; font-size: 14px; color: #4682B4;"; // SteelBlue
    private static final String LOG_EVENT_STYLE = "font-family: Arial, sans-serif; font-size: 12px; color: #708090;"; // SlateGray
    private static final String LOG_TIME_STYLE = "font-family: Arial, sans-serif; font-size: 12px; color: #2F4F4F;"; // DarkSlateGray
    private static final String LOG_USER_STYLE = "font-family: Arial, sans-serif; font-size: 12px; color: #20B2AA;"; // LightSeaGreen

    public static void logLogin(String username, String userType) {
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = now.format(formatter);

            // Writing to log file with HTML-like structure for easy styling if viewed in a browser
            writer.write("<div style='" + LOG_HEADER_STYLE + "'>Event: Login</div>");
            writer.write("<div style='" + LOG_USER_STYLE + "'>Username: " + username + " (UserType: " + userType + ")</div>");
            writer.write("<div style='" + LOG_TIME_STYLE + "'>Time: " + formattedTime + "</div>");
            writer.write("<br>");  // Adding line break for separation

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logRegister(String username, String userType) {
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = now.format(formatter);

            // Writing to log file with HTML-like structure for easy styling if viewed in a browser
            writer.write("<div style='" + LOG_HEADER_STYLE + "'>Event: Register</div>");
            writer.write("<div style='" + LOG_USER_STYLE + "'>Username: " + username + " (UserType: " + userType + ")</div>");
            writer.write("<div style='" + LOG_TIME_STYLE + "'>Time: " + formattedTime + "</div>");
            writer.write("<br>");  // Adding line break for separation

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
