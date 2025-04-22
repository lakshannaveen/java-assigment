package ui;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class AdminUserAccountsStyle {

    // Apply styles to a generic JComponent (like JLabel, JTextField, etc.)
    public static void applyStyle(JComponent component) {
        component.setFont(new Font("Arial", Font.PLAIN, 14));
        component.setForeground(Color.BLACK);
    }

    // Apply custom styles to JFrame
    public static void applyStyle(JFrame frame) {
        frame.getContentPane().setBackground(Color.LIGHT_GRAY); // Light background for the frame
        frame.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.setForeground(Color.BLACK);
    }

    // Apply style to JTable
    public static void applyTableStyle(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 12)); // Table text font
        table.setRowHeight(30); // Row height for better visibility
        table.setGridColor(Color.GRAY); // Grid color for clarity
        table.setSelectionBackground(new Color(30, 144, 255)); // Blue selection color
        table.setSelectionForeground(Color.WHITE); // White text on selection
    }

    // Apply style to Buttons
    public static void applyButtonStyle(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Larger font size for better readability
        button.setBackground(new Color(30, 144, 255)); // Blue background
        button.setForeground(Color.WHITE); // White text color
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Border for button
        button.setFocusPainted(false); // Remove focus painting
        button.setPreferredSize(new Dimension(200, 50)); // Larger button size
        button.setUI(new BasicButtonUI()); // Remove default button style

        // Hover effect using MouseListener
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180)); // Change background on hover (light blue)
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 144, 255)); // Revert back to original color
            }
        });
    }

    // Apply style to JPanel
    public static void applyPanelStyle(JPanel panel) {
        panel.setBackground(Color.WHITE); // White background for panels
    }
}
