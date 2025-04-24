package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminDashboardStyle {

    public static void applyFrameStyle(JFrame frame) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);
        frame.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    public static void applyHeaderStyle(JLabel label) {
        label.setFont(new Font("Verdana", Font.BOLD, 20));
        label.setForeground(new Color(30, 30, 60));
        label.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
    }

    public static void applyButtonStyle(JButton button) {
        Color normalColor = new Color(70, 130, 180);
        button.setBackground(normalColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(normalColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
                button.setForeground(Color.WHITE);
            }
        });
    }

    public static void applyLogoutButtonStyle(JButton button) {
        Color normalColor = new Color(220, 53, 69); // Red color
        Color hoverColor = new Color(200, 35, 51);  // Darker red

        button.setBackground(normalColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
}