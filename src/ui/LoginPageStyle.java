package ui;

import java.awt.Color;
import javax.swing.*;
import java.awt.Font;

public class LoginPageStyle {

    public static void applyStyles(JPanel panel, JTextField emailField, JPasswordField passwordField, JButton loginButton, JButton backButton) {
        // Set background color for the panel
        panel.setBackground(new Color(245, 245, 245));

        // Style the email field
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setForeground(Color.GRAY);
        emailField.setText("Enter Email");
        emailField.setForeground(Color.GRAY);
        emailField.setCaretColor(Color.BLACK); // Change caret color to black
        emailField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (emailField.getText().equals("Enter Email")) {
                    emailField.setText("");
                    emailField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("Enter Email");
                    emailField.setForeground(Color.GRAY);
                }
            }
        });

        // Style the password field
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setForeground(Color.GRAY);
        passwordField.setEchoChar('*');
        passwordField.setText("Enter Password");
        passwordField.setForeground(Color.GRAY);
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).equals("Enter Password")) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("Enter Password");
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });

        // Style the login button
        loginButton.setBackground(new Color(0, 128, 0)); // Green color
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Style the back button (left top as back icon)
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setForeground(new Color(0, 128, 0)); // Green color
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
    }
}