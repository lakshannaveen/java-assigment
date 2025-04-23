package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ExpenseStyle {
    private static final Color green = new Color(46, 204, 113); // Green
    private static final Color greenDark = new Color(39, 174, 96);
    private static final Font fontInput = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font fontPlaceholder = new Font("SansSerif", Font.ITALIC, 14);
    private static final Font fontButton = new Font("SansSerif", Font.BOLD, 16);

    public static void styleSubmitButton(JButton button) {
        button.setFont(fontButton);
        button.setBackground(green);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 45));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(greenDark);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(green);
            }
        });
    }

    public static void styleBackButton(JButton button) {
        button.setFont(fontButton);
        button.setForeground(greenDark);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(greenDark, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(green);
                button.setBorder(BorderFactory.createLineBorder(green, 2));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(greenDark);
                button.setBorder(BorderFactory.createLineBorder(greenDark, 2));
            }
        });
    }

    public static void addPlaceholder(JTextField textField, String placeholder) {
        textField.setFont(fontPlaceholder);
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);

        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setFont(fontInput);
                    textField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setFont(fontPlaceholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }
}
