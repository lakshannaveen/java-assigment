package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;

public class HomePageStyle {

    public static void applyStyles(JLabel welcomeLabel, JButton loginButton, JButton registerButton, JPanel panel) {
        // Set styles for components
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.DARK_GRAY);

        // Set button styles with green color
        loginButton.setPreferredSize(new Dimension(100, 40));
        registerButton.setPreferredSize(new Dimension(100, 40));
        loginButton.setBackground(new Color(34, 139, 34)); // Green color
        registerButton.setBackground(new Color(34, 139, 34)); // Green color
        loginButton.setForeground(Color.WHITE);
        registerButton.setForeground(Color.WHITE);

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
