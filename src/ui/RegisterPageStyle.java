package ui;

import java.awt.Color;
import javax.swing.*;
import java.awt.Font;

public class RegisterPageStyle {

    public static void applyStyles(JPanel panel, JTextField nameField, JTextField emailField, JPasswordField passwordField, JButton registerButton, JButton backButton) {
        // Set background color for the panel
        panel.setBackground(new Color(245, 245, 245));

        // Style the text fields and password field
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setForeground(Color.GRAY);
        nameField.setText("Enter Name");
        nameField.setCaretColor(Color.BLACK); // Change caret color to black
        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (nameField.getText().equals("Enter Name")) {
                    nameField.setText("");
                    nameField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (nameField.getText().isEmpty()) {
                    nameField.setText("Enter Name");
                    nameField.setForeground(Color.GRAY);
                }
            }
        });

        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setForeground(Color.GRAY);
        emailField.setText("Enter Email");
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

        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setForeground(Color.GRAY);
        passwordField.setEchoChar('*');
        passwordField.setText("Enter Password");
        passwordField.setCaretColor(Color.BLACK); // Change caret color to black
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

        // Style the register button
        registerButton.setBackground(new Color(0, 128, 0)); // Green color
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Style the back button (left top as back icon)
        backButton.setIcon(new ImageIcon("path_to_back_icon.png"));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
    }
}