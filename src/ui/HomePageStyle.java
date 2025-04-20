package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class HomePageStyle {

    public static void applyStyles(JLabel titleLabel, JLabel welcomeLabel, JButton loginButton,
                                   JButton registerButton, JPanel panel) {
        // Set styles for components
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(34, 139, 34)); // Green color

        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.DARK_GRAY);

        // Set button styles with green color
        loginButton.setPreferredSize(new Dimension(120, 40));
        registerButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setBackground(new Color(34, 139, 34)); // Green color
        registerButton.setBackground(new Color(34, 139, 34)); // Green color
        loginButton.setForeground(Color.WHITE);
        registerButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Set button borders and make them rounded
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
        loginButton.setFocusPainted(false);
        registerButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Set panel background
        panel.setBackground(new Color(230, 240, 250)); // Light background color

        // Add animation effect for welcome label (fade-in)
        fadeInEffect(welcomeLabel);
    }

    private static void fadeInEffect(JLabel welcomeLabel) {
        // Fade-in animation effect for the label
        Timer timer = new Timer(30, new ActionListener() {
            int alpha = 0; // Starting transparency (fully transparent)

            @Override
            public void actionPerformed(ActionEvent e) {
                if (alpha < 255) {
                    alpha += 5; // Increase transparency
                    welcomeLabel.setForeground(new Color(0, 0, 0, alpha)); // Update label color with alpha (transparency)
                } else {
                    ((Timer) e.getSource()).stop(); // Stop the timer when the fade-in is complete
                }
            }
        });
        timer.start(); // Start the fade-in timer
    }
}