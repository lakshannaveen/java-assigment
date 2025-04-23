package ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class AdminUserAccountsStyle {

    // Apply modern styles to general JComponents
    public static void applyModernStyle(JComponent component) {
        component.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        component.setForeground(Color.BLACK);
    }

    // Apply style to JFrame
    public static void applyStyle(JFrame frame) {
        frame.getContentPane().setBackground(new Color(248, 248, 248));
        frame.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        frame.setForeground(Color.BLACK);
    }

    // Apply modern style to JTable
    public static void applyModernTableStyle(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(40);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(30, 144, 255));
        table.setSelectionForeground(Color.WHITE);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);

        table.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        table.setTableHeader(null);
    }

    // Apply new dark modern button style to match second screenshot
    public static void applyModernButtonStyle(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(0, 51, 102)); // Dark blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        button.setUI(new BasicButtonUI());
        button.setBorder(new RoundedBorder(15)); // rounded border

        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 76, 153)); // slightly lighter on hover
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 51, 102));
            }
        });
    }

    // Apply white background to panel
    public static void applyPanelStyle(JPanel panel) {
        panel.setBackground(Color.WHITE);
    }

    // Rounded border class
    static class RoundedBorder extends AbstractBorder {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(0, 51, 102)); // border color same as background
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = this.radius;
            return insets;
        }
    }
}
