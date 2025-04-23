package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomePageStyle {

    public static void applyStyles(JLabel titleLabel, JLabel welcomeLabel,
                                   JButton loginButton, JButton registerButton,
                                   JPanel mainPanel, JComboBox<String> languageComboBox) {

        // Apply main background
        mainPanel.setBackground(new Color(204, 255, 204)); // Light green

        // Title Label
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 100, 0)); // Dark green
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Welcome Label
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomeLabel.setForeground(Color.DARK_GRAY);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Buttons with rounded style and hover effect
        styleGreenButton(loginButton);
        styleGreenButton(registerButton);

        // Style ComboBox for language selection
        styleComboBox(languageComboBox);
    }

    public static void styleLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(0, 102, 0)); // Forest green
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(0, 102, 0));
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 0), 1));

        // Apply hover effect to combo box
        comboBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                comboBox.setBackground(new Color(240, 255, 240)); // Light green on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                comboBox.setBackground(Color.WHITE); // Reset to white when not hovered
            }
        });
    }

    private static void styleGreenButton(JButton button) {
        Color baseColor = new Color(34, 139, 34);   // Forest Green
        Color hoverColor = new Color(50, 160, 50);  // Brighter Green

        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            boolean hovered = false;

            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                button.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                hovered = false;
                button.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                hovered = button.getBounds().contains(e.getPoint());
                button.repaint();
            }

            public boolean isHovered() {
                return hovered;
            }
        });

        button.setUI(new RoundedButtonUI(baseColor, hoverColor));
    }

    // Custom Button UI to paint rounded buttons
    private static class RoundedButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
        private final Color baseColor;
        private final Color hoverColor;

        public RoundedButtonUI(Color baseColor, Color hoverColor) {
            this.baseColor = baseColor;
            this.hoverColor = hoverColor;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ButtonModel model = b.getModel();

            Color fillColor = model.isRollover() ? hoverColor : baseColor;
            g2.setColor(fillColor);
            g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 30, 30);

            FontMetrics fm = b.getFontMetrics(b.getFont());
            Rectangle r = b.getBounds();
            String text = b.getText();
            g2.setColor(b.getForeground());
            g2.drawString(text,
                    (r.width - fm.stringWidth(text)) / 2,
                    (r.height + fm.getAscent()) / 2 - 2);

            g2.dispose();
        }
    }
}
