package ui;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final String LOG_FILE_PATH = "login_logs.txt";

    public static void logLogin(String username) {
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            writer.write("User: " + username + ", Login Time: " + now.format(formatter) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}