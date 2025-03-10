package ui;

import javax.swing.*;
import java.awt.*;

public class AdminStyles {

    public static void applyStyles(JPanel formPanel, JTextField usernameField, JPasswordField passwordField, JButton loginButton) {
        // Set background color for the form panel
        formPanel.setBackground(Color.WHITE);

        // Style the username and password fields
        usernameField.setBackground(Color.LIGHT_GRAY);
        passwordField.setBackground(Color.LIGHT_GRAY);

        // Style the login button
        loginButton.setBackground(Color.BLUE);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(100, 40));
    }
}
