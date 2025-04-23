package ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPageStyle {

    public static void applyStyles(JPanel panel, JTextField emailField, JPasswordField passwordField, JButton loginButton, JButton backButton) {
        // Panel background
        panel.setBackground(new Color(250, 250, 250));

        // Common font
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Email field styling
        emailField.setFont(inputFont);
        emailField.setForeground(Color.GRAY);
        emailField.setText("Enter Email");
        emailField.setCaretColor(Color.BLACK);
        emailField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

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

        // Password field styling
        passwordField.setFont(inputFont);
        passwordField.setForeground(Color.GRAY);
        passwordField.setText("Enter Password");
        passwordField.setEchoChar((char) 0);
        passwordField.setCaretColor(Color.BLACK);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).equals("Enter Password")) {
                    passwordField.setText("");
                    passwordField.setEchoChar('•');
                    passwordField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("Enter Password");
                    passwordField.setEchoChar((char) 0);
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });

        // Login button styling
        loginButton.setBackground(new Color(46, 204, 113)); // Green
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(new RoundedBorder(10));

        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                loginButton.setBackground(new Color(39, 174, 96));
            }

            public void mouseExited(MouseEvent evt) {
                loginButton.setBackground(new Color(46, 204, 113));
            }
        });

        // Back button styling
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backButton.setForeground(new Color(39, 174, 96));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                backButton.setForeground(new Color(22, 160, 133));
            }

            public void mouseExited(MouseEvent evt) {
                backButton.setForeground(new Color(39, 174, 96));
            }
        });
    }

    // Rounded border class for buttons
    static class RoundedBorder extends LineBorder {
        private int radius;

        public RoundedBorder(int radius) {
            super(new Color(39, 174, 96), 1, true);
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
}
