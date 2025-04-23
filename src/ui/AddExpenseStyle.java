package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddExpenseStyle {

    public static void styleButton(JButton button) {
        Color normalColor = new Color(34, 139, 34);
        Color hoverColor = new Color(50, 160, 50);

        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(normalColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(28, 125, 68), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }

    public static void addPlaceholder(JTextField textField, String placeholder) {
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);
        textField.setFont(new Font("SansSerif", Font.ITALIC, 14));

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                    textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                    textField.setFont(new Font("SansSerif", Font.ITALIC, 14));
                }
            }
        });
    }
}
