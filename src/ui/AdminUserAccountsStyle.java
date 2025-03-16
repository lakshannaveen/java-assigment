package ui;

import javax.swing.*;
import java.awt.*;

public class AdminUserAccountsStyle {

    public static void applyStyle(JComponent component) {
        // Apply custom styles to the component
        component.setFont(new Font("Arial", Font.PLAIN, 14));
        component.setForeground(Color.BLACK);
    }

    public static void applyStyle(JFrame frame) {
        // Apply custom styles to the JFrame
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);
        frame.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.setForeground(Color.BLACK);
    }
}