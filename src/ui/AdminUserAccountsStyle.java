package ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class AdminUserAccountsStyle {

    public static void applyModernStyle(JComponent component) {
        component.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        component.setForeground(Color.BLACK);
    }

    public static void applyStyle(JFrame frame) {
        frame.getContentPane().setBackground(new Color(248, 248, 248));
        frame.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        frame.setForeground(Color.BLACK);
    }

    public static void applyModernTableStyle(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(32);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(70, 130, 180)); // Updated color
        table.setSelectionForeground(Color.WHITE);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));
            header.setBackground(new Color(230, 245, 250));
            header.setForeground(new Color(70, 130, 180)); // Updated color
            header.setOpaque(true);
        }
    }

    public static void applyModernButtonStyle(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180)); // Updated color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(140, 40));
        button.setUI(new BasicButtonUI());
        button.setBorder(new RoundedBorder(12));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 115, 160)); // Slightly darker on hover
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180)); // Original color
            }
        });
    }

    public static void applyPanelStyle(JPanel panel) {
        panel.setBackground(Color.WHITE);
    }

    static class RoundedBorder extends AbstractBorder {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(70, 130, 180));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius, this.radius, this.radius, this.radius);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = this.radius;
            return insets;
        }
    }
}
