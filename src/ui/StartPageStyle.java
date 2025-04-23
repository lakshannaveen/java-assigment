package ui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartPageStyle {

    private static final Dimension BUTTON_SIZE = new Dimension(150, 40);
    private static final Dimension SEARCH_FIELD_SIZE = new Dimension(150, 30); // Narrower search field
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font SEARCH_FONT = new Font("Arial", Font.PLAIN, 14);

    public static void styleGreenButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setBackground(new Color(46, 125, 50)); // Darker green
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(56, 142, 60)); // Lighter green
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(46, 125, 50)); // Original green
            }
        });
    }

    public static void styleRedButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setBackground(new Color(198, 40, 40)); // Darker red
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(229, 57, 53)); // Lighter red
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(198, 40, 40)); // Original red
            }
        });
    }

    public static void styleBlueButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setBackground(new Color(25, 118, 210)); // Darker blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(30, 136, 229)); // Lighter blue
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(25, 118, 210)); // Original blue
            }
        });
    }

    public static void styleSearchField(JTextField searchField) {
        searchField.setFont(SEARCH_FONT);
        searchField.setPreferredSize(SEARCH_FIELD_SIZE);
        searchField.setMinimumSize(SEARCH_FIELD_SIZE);
        searchField.setMaximumSize(SEARCH_FIELD_SIZE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    public static void styleTable(JTable table) {
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setGridColor(new Color(200, 200, 200));
        table.setShowGrid(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(34, 139, 34));
        header.setForeground(Color.WHITE);

        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(150);
        }
    }
}