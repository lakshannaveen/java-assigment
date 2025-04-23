package ui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartPageStyle {

    private static final Dimension BUTTON_SIZE = new Dimension(150, 40);
    private static final Dimension SEARCH_FIELD_SIZE = new Dimension(150, 30);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font SEARCH_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font WELCOME_FONT = new Font("Serif", Font.BOLD, 24);
    private static final Font CLOCK_FONT = new Font("Serif", Font.BOLD, 16);

    public static void styleGreenButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setBackground(new Color(46, 125, 50));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(56, 142, 60));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(46, 125, 50));
            }
        });
    }

    public static void styleRedButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setBackground(new Color(198, 40, 40));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(229, 57, 53));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(198, 40, 40));
            }
        });
    }

    public static void styleBlueButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setBackground(new Color(25, 118, 210));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(30, 136, 229));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(25, 118, 210));
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

    public static void styleWelcomePanel(JPanel panel) {
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    public static void styleWelcomeLabel(JLabel label) {
        label.setFont(WELCOME_FONT);
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public static void styleClockPanel(JPanel panel) {
        panel.setBackground(Color.BLACK);
    }

    public static void styleClockLabel(JLabel label) {
        label.setFont(CLOCK_FONT);
        label.setForeground(Color.WHITE);
    }

    public static void styleTable(JTable table) {
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setGridColor(new Color(200, 200, 200));
        table.setShowGrid(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(Color.BLACK);
        header.setForeground(Color.WHITE);

        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(150);
        }
    }

    // 🔧 Added this method to style JComboBox
    public static void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(SEARCH_FONT);
        comboBox.setPreferredSize(SEARCH_FIELD_SIZE);
        comboBox.setMinimumSize(SEARCH_FIELD_SIZE);
        comboBox.setMaximumSize(SEARCH_FIELD_SIZE);
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(Color.BLACK);
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    }
}
