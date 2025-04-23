package ui;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Color lightGreen = new Color(204, 255, 204);
        Color softBlack = new Color(40, 40, 40);  // soft black

        int width = getWidth();
        int height = getHeight();

        GradientPaint gp = new GradientPaint(0, 0, lightGreen, 0, height, softBlack);
        g2.setPaint(gp);
        g2.fillRect(0, 0, width, height);
    }
}
