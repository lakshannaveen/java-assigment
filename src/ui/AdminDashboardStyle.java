package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboardStyle {

    public static void applyFrameStyle(JFrame frame) {
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

    public static void applyButtonStyle(JButton button) {
        // Custom styling for buttons
        button.setBackground(new Color(70, 130, 180)); // SteelBlue color
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Add padding for button text

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 150, 220)); // Lighter blue on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180)); // Reset to original
            }
        });
    }

    public static void applyHeaderStyle(JLabel label) {
        // Custom styling for labels
        label.setForeground(new Color(70, 130, 180)); // SteelBlue color
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Add some space above and below
    }
}
