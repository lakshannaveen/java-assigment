package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterPageStyle {

    public static void applyStyles(JPanel panel, JTextField nameField, JTextField emailField, JPasswordField passwordField, JButton registerButton, JButton backButton) {
        panel.setBackground(Color.WHITE);

        Font placeholderFont = new Font("Segoe UI", Font.PLAIN, 13);
        Color placeholderColor = Color.GRAY;
        Color textColor = Color.BLACK;

        setupPlaceholder(nameField, "Enter Name", placeholderFont, placeholderColor, textColor);
        setupPlaceholder(emailField, "Enter Email", placeholderFont, placeholderColor, textColor);
        setupPasswordPlaceholder(passwordField, "Enter Password", placeholderFont, placeholderColor, textColor);

        // Register Button Styling
        Color loginGreen = new Color(40, 167, 69);           // Same as login
        Color loginGreenHover = new Color(33, 136, 56);      // Hover color

        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerButton.setBackground(loginGreen);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(new EmptyBorder(8, 20, 8, 20));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(loginGreenHover);
            }

            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(loginGreen);
            }
        });

        // Back Button Styling
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backButton.setForeground(loginGreen);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private static void setupPlaceholder(JTextField field, String placeholder, Font font, Color placeholderColor, Color textColor) {
        field.setFont(font);
        field.setForeground(placeholderColor);
        field.setText(placeholder);
        field.setCaretColor(textColor);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(textColor);
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(placeholderColor);
                }
            }
        });
    }

    private static void setupPasswordPlaceholder(JPasswordField field, String placeholder, Font font, Color placeholderColor, Color textColor) {
        field.setFont(font);
        field.setForeground(placeholderColor);
        field.setText(placeholder);
        field.setEchoChar((char) 0); // Show text for placeholder
        field.setCaretColor(textColor);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(textColor);
                    field.setEchoChar('•'); // Use dot for password
                }
            }

            public void focusLost(FocusEvent e) {
                if (String.valueOf(field.getPassword()).trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(placeholderColor);
                    field.setEchoChar((char) 0); // Show placeholder again
                }
            }
        });
    }
}
