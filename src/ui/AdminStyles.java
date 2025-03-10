package ui;

import javax.swing.*;
import java.awt.*;

public class AdminStyles {

    public static void applyStyles(JPanel formPanel, JTextField usernameField, JPasswordField passwordField, JButton loginButton) {
        // Set background color for the form panel
        formPanel.setBackground(new Color(245, 245, 245)); // Light gray background

        // Style the username and password fields
        usernameField.setBackground(Color.WHITE);
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setMargin(new Insets(5, 10, 5, 10));

        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setMargin(new Insets(5, 10, 5, 10));

        // Style the login button
        loginButton.setBackground(new Color(0, 123, 255)); // Bootstrap primary blue
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Add a shadow effect to the button
        loginButton.setUI(new JButton().getUI());
        loginButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 123, 255), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}