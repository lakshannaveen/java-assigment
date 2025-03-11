package ui;

import javax.swing.*;
import java.awt.*;

public class AdminRegisterStyle {

    public static void applyStyles(JButton registerButton, JButton backButton) {
        // Register button (Blue color)
        registerButton.setBackground(new Color(0, 102, 204)); // Blue
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Back button (Gray color)
        backButton.setBackground(new Color(150, 150, 150)); // Gray
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
    }
}
