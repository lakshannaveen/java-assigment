package ui;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardStyle {

    public static void applyStyle(JFrame frame) {
        // Set the look and feel to the system's look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Custom styling for frame
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);
        frame.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    public static void applyStyle(JButton button) {
        // Custom styling for buttons
        button.setBackground(Color.BLUE);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    public static void applyStyle(JLabel label) {
        // Custom styling for labels
        label.setForeground(Color.BLUE);
        label.setFont(new Font("Arial", Font.BOLD, 16));
    }
}