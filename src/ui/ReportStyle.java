package ui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;

public class ReportStyle {

    public static void styleGreenButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 16));
        button.setBackground(new Color(34, 139, 34)); // Dark green
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(100, 30));
    }

    public static void styleBlueButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 16));
        button.setBackground(new Color(30, 144, 255)); // Dodger blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(120, 30));
    }

    public static void styleTable(JTable table) {
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setGridColor(new Color(200, 200, 200));
        table.setShowGrid(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(34, 139, 34)); // Dark green
        header.setForeground(Color.WHITE);

        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(150);
        }
    }
}
